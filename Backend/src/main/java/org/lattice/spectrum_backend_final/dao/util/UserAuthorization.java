package org.lattice.spectrum_backend_final.dao.util;

import javax.ws.rs.core.HttpHeaders;

import org.lattice.spectrum_backend_final.dao.constants.ApiConstant;
import org.lattice.spectrum_backend_final.dao.manager.DbConnectionManager;

public final class UserAuthorization {

	public boolean isUserRoleEqualsManager(HttpHeaders headers) throws Exception {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;
		}
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (BasicUtility.getUserRole(DbConnectionManager.getInstance().getTokenManager().getUsername())
						.contains(ApiConstant.MANAGER))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}
		return false;
	}

	public boolean isUserRoleEqualsSuperAdmin(HttpHeaders headers) throws Exception {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;
		}
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (BasicUtility.getUserRole(DbConnectionManager.getInstance().getTokenManager().getUsername())
						.contains(ApiConstant.SUPER_ADMIN))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}
		return false;
	}

	public boolean isUserRoleEqualsAdminSuperAdminAuditor(HttpHeaders headers) {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;

		} else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (DbConnectionManager.getInstance().getTokenManager().getUserType().equals(ApiConstant.ADMIN)
						|| DbConnectionManager.getInstance().getTokenManager().getUserType().equals(ApiConstant.AUDITOR)
						|| DbConnectionManager.getInstance().getTokenManager().getUserType()
								.equals(ApiConstant.SUPER_ADMIN))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}

		return false;
	}

	public boolean isUserRoleEqualsAdminSuperAdmin(HttpHeaders headers) {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;

		} else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (DbConnectionManager.getInstance().getTokenManager().getUserType().equals(ApiConstant.ADMIN)
						|| DbConnectionManager.getInstance().getTokenManager().getUserType()
								.equals(ApiConstant.SUPER_ADMIN))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}

		return false;
	}

	public boolean isUserRoleEqualsManagerTechnician(HttpHeaders headers) {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;

		} else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (ApiConstant.MANAGER
						.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType())
						|| ApiConstant.TECHNICIAN
								.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType()))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}

		return false;
	}

	public boolean isUserRoleEqualsManagerTechnicianAuditor(HttpHeaders headers) {
		if (headers.getRequestHeader(ApiConstant.AUTHORIZATION) == null) {
			return false;

		} else if (headers.getRequestHeader(ApiConstant.AUTHORIZATION).get(0)
				.equals(DbConnectionManager.getInstance().getTokenManager().getToken())
				&& (ApiConstant.MANAGER
						.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType())
						|| ApiConstant.TECHNICIAN
								.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType())
						|| ApiConstant.AUDITOR
								.equalsIgnoreCase(DbConnectionManager.getInstance().getTokenManager().getUserType()))
				&& !DbConnectionManager.getInstance().getTokenManager().isSessionTimeout()
		) {
			return true;
		}

		return false;
	}
	
}
