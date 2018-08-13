package com.dennislin.demos.git_util;

import com.dennislin.demos.git_util.classifiers.CalendarClassifier;
import com.dennislin.demos.git_util.classifiers.ObjectClassifier;
import com.dennislin.demos.git_util.classifiers.Retriever;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;


public final class Main {
  private static final String PATH_TO_GIT_BINARY = "/usr/bin/git";

  private static final String GIT_LOG_FORMAT = "%H|%ae|%ce|%ad|%cd|%s";

  private static final List<GitEntry> listGitEntries = new LinkedList<>();

  /**
   * @return the command-line parser options.
   */
  private static Options buildOptions() {
    Options options = new Options();
    options.addOption("u", "user", false, "The git user or email address to search by")
        .addOption("y", "yearly", false, "Group the results by year")
        .addOption("m", "monthly", false, "Group the results by month")
        .addOption("d", "daily", false, "Group the results by date");

    return options;
  }

  /**
   * The one and only main function.
   * @param args
   * @throws Exception
   */
  public static final void main(final String[] args) throws Exception {
    Options options = buildOptions();
    CommandLineParser parser = new DefaultParser();

    try {
      CommandLine commandLine = parser.parse(options, args);
      parseGitRepository(commandLine.getArgs()[0]);

      //  The lambda used to retrieve the date field we are interested in from the git log entry
      final Retriever<Calendar> retrieverCalendar = (gitEntry) -> {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(gitEntry.getAuthorDate());

        return calendar;
      };

      Map<Integer, List<GitEntry>> mapResults = null;
      if (commandLine.hasOption("d")) {
        mapResults = (new CalendarClassifier()).classify(Calendar.DAY_OF_WEEK, retrieverCalendar, listGitEntries);
      } else if (commandLine.hasOption("m")) {
        mapResults = (new CalendarClassifier()).classify(Calendar.MONTH, retrieverCalendar, listGitEntries);
      } else if (commandLine.hasOption("y")) {
        mapResults = (new CalendarClassifier()).classify(Calendar.YEAR, retrieverCalendar, listGitEntries);
      }

      if (commandLine.hasOption("u")) {
        //  The lambda used to retrieve the String field we are interested in from the git log entry
        final Retriever<String> retrieverAuthorEmail = (gitEntry) -> {
          return gitEntry.getAuthorEmail();
        };

        Map<Integer, Map<Object, List<GitEntry>>> mapUserResults = new HashMap<>();
        for (Map.Entry<Integer, List<GitEntry>> entry : mapResults.entrySet()) {
          Map<Object, List<GitEntry>> mapUsers = (new ObjectClassifier()).classify(retrieverAuthorEmail, entry.getValue());
          mapUserResults.put(entry.getKey(), mapUsers);
        }

        dumpEmbeddedMap(mapUserResults);
      } else {
        dumpMap(mapResults);
      }

    } catch (ParseException exception) {
      System.err.println("Parsing error: " + exception.getMessage());
    }
  }

  /**
   * Processes "git log" entries and converts them into objects for manipulation by the app.
   *
   * @param targetDirectory the Git repository (a local directory) to parse
   * @throws Exception
   */
  private static void parseGitRepository(final String targetDirectory) throws Exception {
    Runtime rt = Runtime.getRuntime();
    String command = String.format("%s --git-dir %s/.git log --pretty=format:\"%s\"", PATH_TO_GIT_BINARY, targetDirectory, GIT_LOG_FORMAT);
    Process process = rt.exec(command);
    InputStream is = process.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

    String line = reader.readLine();
    if (null != line) {
      while (null != line) {
        StringTokenizer tokenizer = new StringTokenizer(line, "|");
        String commitHash = tokenizer.nextToken();
        String authorEmail = tokenizer.nextToken();
        String committerEmail = tokenizer.nextToken();
        String authorDate = tokenizer.nextToken();
        String committerDate = tokenizer.nextToken();
        String subject = tokenizer.nextToken();

        GitEntry newEntry = new GitEntry(commitHash, authorEmail, committerEmail, authorDate, committerDate, subject);
        listGitEntries.add(newEntry);
        line = reader.readLine();
      }
    } else {
      reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
      line = reader.readLine();
      while (null != line) {
        System.out.println("Error: " + line);
        line = reader.readLine();
      }
    }
  }

  private static void dumpMap(final Map map) {
    System.out.println("**** DUMPING MAP");
    for (Object key : map.keySet()) {
      System.out.println("  " + key);

      List<?> listValues = (List<?>) map.get(key);
      for (Object value : listValues) {
        System.out.println("    " + value);
      }
    }
  }

  private static void dumpEmbeddedMap(final Map map) {
    System.out.println("**** DUMPING EMBEDDED MAP");
    for (Object key : map.keySet()) {
      System.out.println("  " + key);

      Map embeddedMap = (Map) map.get(key);
      for (Object embeddedKey : embeddedMap.keySet()) {
        System.out.println("    " + embeddedKey);
        List<?> listValues = (List<?>) embeddedMap.get(embeddedKey);
        for (Object value : listValues) {
          System.out.println("      " + value);
        }
      }
    }
  }
}

