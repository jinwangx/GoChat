package org.heima.chat.service.impl;

import java.util.List;

import org.heima.chat.dao.InvitationDao;
import org.heima.chat.pojo.Invitation;
import org.heima.chat.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationServiceImpl	implements
									InvitationService
{

	@Autowired
	InvitationDao	invitationDao;

	@Override
	public void addInvitation(Invitation invitation) {
		Invitation i = invitationDao.findInvitationBySender(invitation.getSender());
		if (i != null) {
			i.setContent(invitation.getContent());
			invitationDao.updateInvitation(i);
		} else {
			invitationDao.addInvitation(invitation);
		}
	}

	@Override
	public void updateInvitation(Invitation invitation) {
		if (invitation.getId() == null) {
			addInvitation(invitation);
		} else {
			invitationDao.updateInvitation(invitation);
		}
	}

	@Override
	public List<Invitation> findPendingInvitation(String account) {
		List<Invitation> list1 = invitationDao.findInvitationByAccountAndState(account, 0);
		List<Invitation> list2 = invitationDao.findInvitationByAccountAndState(account, 3);
		if (list1 != null && list2 != null) {
			list1.addAll(list2);
			return list1;
		} else if (list1 != null && list2 == null) {
			return list1;
		} else if (list1 == null && list2 != null) { return list2; }
		return null;
	}

	@Override
	public Invitation findInvitationBySender(String sender) {
		return invitationDao.findInvitationBySender(sender);
	}

}
