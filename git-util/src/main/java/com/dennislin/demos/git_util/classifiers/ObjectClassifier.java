package com.dennislin.demos.git_util.classifiers;

import com.dennislin.demos.git_util.GitEntry;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

/**
 * Classifier used to classify a list of entries by any non-date related value.
 */
public class ObjectClassifier {
  public Map<Object, List<GitEntry>> classify(final Retriever<? extends Object> retriever,
                                               final List<GitEntry> listGitEntries) {
    Map<Object, List<GitEntry>> mapResults = new HashMap<>();

    for (GitEntry gitEntry : listGitEntries) {
      Object key = retriever.get(gitEntry);
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
