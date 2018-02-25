package it.univaq.msa.pwrtool.business.impl;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.jgit.api.CreateBranchCommand.SetupUpstreamMode;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.univaq.msa.pwrtool.business.GitService;

@Service
public class GitServiceImpl implements GitService  {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${gitlocal}")
	private String localPath;

	@Value("${gitremote}")
	private String remotePath;

	private Repository localRepo;

	private Git git;


	@PostConstruct
	void initRepository() throws IOException, GitAPIException {
//	logger.info("inside git service");
//	
		localRepo = new FileRepository(localPath + "/.git");
//		
		git = new Git(localRepo);
//		
//		this.gitClone();
		
		

	}


	@Override
	public void gitCreate() throws IOException {
		Repository newRepo = new FileRepository(localPath + ".git");
		newRepo.create();
	}


	@Override
	public void gitClone() throws IOException, GitAPIException {
		this.git = Git.cloneRepository().setURI(remotePath).setDirectory(new File(localPath)).call();
	}

	
	@Override
	public void gitAdd() throws IOException, GitAPIException {
		File myfile = new File(localPath + "/myfile");
		myfile.createNewFile();
		git.add().addFilepattern("myfile").call();
	}

	
	@Override
	public void gitCommit() throws IOException, GitAPIException, JGitInternalException {
		git.commit().setMessage("Added myfile").call();
	}

	
	@Override
	public void gitPush() throws IOException, JGitInternalException, GitAPIException {
		git.push().call();
	}

	
	@Override
	public void gitTrackMaster() throws IOException, JGitInternalException, GitAPIException {
		git.branchCreate().setName("master").setUpstreamMode(SetupUpstreamMode.SET_UPSTREAM)
				.setStartPoint("origin/master").setForce(true).call();
	}

	
	@Override
	public void gitPull() throws IOException, GitAPIException {
		git.pull().call();
	}
	
	@Override
	public Git getGit() {
		return git;
	}
	
	@Override
	public void setGit(Git git) {
		this.git = git;
	}


	@Override
	public Iterable<RevCommit> getListOfCommit() throws NoHeadException, GitAPIException, IOException {
		Iterable<RevCommit> result = git.log().all().call();
		return result;
	}
	
	@Override
	public Iterable<RevCommit> getListOfCommitByPath(String path) throws NoHeadException, GitAPIException, IOException {
		Iterable<RevCommit> result = git.log().addPath(path).call();
		return result;
	}
	
	
	@Override
	public void deleteRepository() throws IOException{
		
		this.git.clean();
		this.git.getRepository().close();
		this.git.close();
//		this.remoteGit.getRepository().close();
		
		
		org.apache.commons.io.FileUtils.deleteDirectory(new File(localPath));
	}
	
	private void deleteRecursive(File f) throws IOException{
       
    if(!f.isDirectory()){
    	FileUtils.delete(f);
    }        
    else {
    	 for (File t : f.listFiles())
    	 {
    		 deleteRecursive(t);
    	 }
    	
    }
		
	}
	
}
