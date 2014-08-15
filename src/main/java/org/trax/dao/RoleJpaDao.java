package org.trax.dao;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.trax.model.Role;
import org.trax.model.User;

@Repository("roleDao")
public class RoleJpaDao extends GenericJpaDao<Role, Long> implements RoleDao
{
	public Role findRole(String roleName)
	{
		String hql = "from Role role where name = :roleName";
		Role role = (Role) entityManager.createQuery(hql).setParameter("roleName", roleName).getSingleResult();
		
		return role;
	}
	
    public Role findByName(String roleName)
    {
        String hql = "from Role where name = :roleName";
        List<Role> roles = entityManager.createQuery(hql).setParameter("roleName", roleName).getResultList();

        return roles.size() > 0 ? roles.get(0) : null;
    }

    public Role findDuplicateRole(Role role)
    {
        String hql = "from Role role where Role.name = :name and Role.id != :id";
        List<Role> roleList = entityManager.createQuery(hql).setParameter("name", role.getName())
                .setParameter("id", role.getId()).getResultList();

        return roleList.size() > 0 ? roleList.get(0) : null;
    }

 /*   @Override
    public void remove(Role role)
    {

        // remove the roles from all users that have it
        for (User user : role.getMembers())
            user.getRoles().remove(role);
        entityManager.remove(role);
    }
    */
}
