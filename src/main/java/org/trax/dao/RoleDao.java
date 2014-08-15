package org.trax.dao;

import org.trax.model.Role;

public interface RoleDao extends GenericDao<Role, Long>
{

	Role findRole(String roleLeader);
    Role findByName(String adminRoleName);
    
    Role findDuplicateRole(Role role);
    
    void remove(Role role);
}
