package org.lattice.spectrum_backend_final.dao.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;

import com.lattice.spectrum.ComLibrary.utility.sLog;

public final class GitCommitUtil extends Thread {

	private final String GIT_DIRECTORY_LOCATION = "../.git";

	@Override
	public void run() {
		updateLatestCommit();
	}

	private void updateLatestCommit() {

		try {
			if (!isGitDirectoryExists()) {
				sLog.d(this, "Git directory not found. LATEST_COMMIT: " + fetchLatestCommit());
				return;
			}

			final String commit = getGitCommitFromDirectory();
			if (commit == null) {
				sLog.d(this, "Error in creating git commit file, please try again.");
				return;
			}

			final boolean isCommitSaved = writeCommitToFile(commit.substring(0, 7));
			if (!isCommitSaved) {
				sLog.d(this, "Error in updating commit value, please try again.");
				return;
			}
			sLog.d(this, "Commit updated, LATEST_COMMIT: " + fetchLatestCommit());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public final String fetchLatestCommit() {
		String commit = null;
		Scanner scanner = null;
		try {
			InputStream inputStream = this.getClass().getClassLoader()
					.getResourceAsStream("org/lattice/spectrum_backend_final/commit");
			scanner = new Scanner(inputStream);
			while (scanner.hasNextLine()) {
				commit = scanner.nextLine();
			}
		} catch (Exception e) {
			e.printStackTrace();
			commit = ApiConstant.NOT_APPLICABLE;
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		return commit;
	}

	private final boolean isGitDirectoryExists() throws IOException {
		final File gitDir = new File(GIT_DIRECTORY_LOCATION);
		return gitDir.exists() && gitDir.isDirectory();
	}

	private final String getGitCommitFromDirectory() throws IOException {
		String latestCommitHash = null;
		Git git = null;
		try {
			Repository repository = new FileRepository(GIT_DIRECTORY_LOCATION);
			git = new Git(repository);
			RevCommit latestCommit = git.log().setMaxCount(1).call().iterator().next();
			latestCommitHash = latestCommit.getName();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (git != null) {
				git.close();
			}
		}
		return latestCommitHash;
	}

	private final boolean writeCommitToFile(String commit) throws Exception {
		BufferedWriter output = null;
		try {
			output = new BufferedWriter(new FileWriter("./src/main/java/org/lattice/spectrum_backend_final/commit"));
			output.write(commit);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (output != null) {
				output.close();
			}
		}
		return false;
	}

}
