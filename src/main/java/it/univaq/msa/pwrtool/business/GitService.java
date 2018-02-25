package it.univaq.msa.pwrtool.business;

import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.revwalk.RevCommit;

public interface GitService {
	
	void gitCreate() throws IOException;

	void gitClone() throws IOException, GitAPIException;

	void gitAdd() throws IOException, GitAPIException;

	void gitCommit() throws IOException, GitAPIException, JGitInternalException;

	void gitPush() throws IOException, JGitInternalException, GitAPIException;

	void gitTrackMaster() throws IOException, JGitInternalException, GitAPIException;

	void gitPull() throws IOException, GitAPIException;
	
	Iterable<RevCommit> getListOfCommit() throws NoHeadException, GitAPIException, IOException;

	Git getGit();

	void setGit(Git git);

	void deleteRepository() throws IOException;

	Iterable<RevCommit> getListOfCommitByPath(String path) throws NoHeadException, GitAPIException, IOException;

}
