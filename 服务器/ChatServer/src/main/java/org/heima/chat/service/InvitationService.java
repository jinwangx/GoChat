package org.heima.chat.service;

import java.util.List;

import org.heima.chat.pojo.Invitation;

public interface InvitationService {

	void addInvitation(Invitation invitation);

	void updateInvitation(Invitation invitation);

	List<Invitation> findPendingInvitation(String account);
	
	Invitation findInvitationBySender(String sender);
}
