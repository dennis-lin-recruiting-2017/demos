package com.dennislin.demos.git_util.classifiers;

import com.dennislin.demos.git_util.GitEntry;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Classifier used to classify a list of entries by a date-related value.
 */
public class CalendarClassifier {
  /**
   * @param calendarField the java.util.Calendar field to classify by
   * @param retriever the lambda used to retrieve the git log entry value to classify  by
   * @param listGitEntries the list of git log entries to classify
   * @return
   */
  public Map<Integer, List<GitEntry>> classify(final int calendarField,
                                              final Retriever<Calendar> retriever,
                                              final List<GitEntry> listGitEntries) {
    Map<Integer, List<GitEntry>> mapResults = new HashMap<>();

    for (GitEntry gitEntry : listGitEntries) {
      Calendar classifierValue = retriever.get(gitEntry);
      int key = classifierValue.get(calendarField);
      List<GitEntry> list = mapResults.get(key);
      if (null == list) {
        list = new LinkedList<GitEntry>();
        mapResults.put(key, list);
      }
      list.add(gitEntry);
    }

    return mapResults;
  }
}