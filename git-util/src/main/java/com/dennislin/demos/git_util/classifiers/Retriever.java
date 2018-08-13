package com.dennislin.demos.git_util.classifiers;

import com.dennislin.demos.git_util.GitEntry;

/**
 * Interface for a generic lambda used to retrieve an arbitrary value from an object.
 *
 * @param <T> the expected type of the data retrieved.
 */
public interface Retriever<T> {
  T get(GitEntry gitEntry);
}
