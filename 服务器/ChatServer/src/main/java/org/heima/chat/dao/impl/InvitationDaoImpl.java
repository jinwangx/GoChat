package org.heima.chat.dao.impl;

import java.util.List;

import org.heima.chat.dao.BaseDaoSupport;
import org.heima.chat.dao.InvitationDao;
import org.heima.chat.pojo.Invitation;
import org.springframework.stereotype.Repository;

@Repository
public class InvitationDaoImpl	extends
								BaseDaoSupport<Invitation>	implements
															InvitationDao
{

	@Override
	public void addInvitation(Invitation invitation) {
		invitation.setId((String) save(invitation));
	}

	@Override
	public void updateInvitation(Invitation invitation) {
		update(invitation);
	}

	@Override
	public List<Invitation> findInvitationByAccountAndState(String account, int state) {
		return findByFields(Invitation.class, new String[] { "receiver", "state" }, new Object[] {
				account,
				state });
	}

	@Override
	public Invitation findInvitationBySender(String sender) {
		return findOneByField(Invitation.class, "sender", sender);
	}

}
