Assumptions:

1.  The "git" binary is located in the path.
2.  The directory supplied is the base of the git repository that contains the Git metadata directory ".git"
3.  No error checking!

To build:

This is a Maven project, so "mvn compile" should work.

To run:

- ./run.sh --daily|--monthly|--yearly [--user] <path-to-git-repository>
