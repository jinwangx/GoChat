package org.heima.chat.dao;

import java.util.List;

import org.heima.chat.pojo.Invitation;

public interface InvitationDao
{
	void addInvitation(Invitation invitation);

	void updateInvitation(Invitation invitation);

	Invitation findInvitationBySender(String sender);

	List<Invitation> findInvitationByAccountAndState(String account, int state);
}
