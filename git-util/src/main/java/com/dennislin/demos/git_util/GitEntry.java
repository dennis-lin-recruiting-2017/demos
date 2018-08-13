package com.dennislin.demos.git_util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The data object used to represent a git log entry.
 */
public final class GitEntry {
  private final String strCommitHash;
  private final String strAuthorEmail;
  private final String strCommitterEmail;
  private final Date objAuthorDate;
  private final Date objCommitterDate;
  private final String strSubject;
  private final SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy ZZZZ");

  @SuppressWarnings("unused")
  private GitEntry() {
    throw new RuntimeException("Should not call default constructor");
  }

  public GitEntry(final String commitHash, final String authorEmail, final String committerEmail,
                  final String authorDate, final String committerDate, final String subject) throws ParseException {
    strCommitHash = commitHash;
    strAuthorEmail = authorEmail;
    strCommitterEmail = committerEmail;
    objAuthorDate = sdf.parse(authorDate);
    objCommitterDate = sdf.parse(committerDate);
    strSubject = subject;
  }

  public String getCommitHash() {
    return strCommitHash;
  }

  public String getAuthorEmail() {
    return strAuthorEmail;
  }

  public String getCommitterEmail() {
    return strCommitterEmail;
  }

  public Date getCommitterDate() {
    return objCommitterDate;
  }

  public Date getAuthorDate() {
    return objAuthorDate;
  }

  public String getSubject() {
    return strSubject;
  }

  @Override
  public String toString() {
    return String.format("%s,%s,%s", getCommitHash(), getAuthorEmail(), getSubject());
  }
}
