package aic12.project3.common.beans;

import java.util.List;

import aic12.project3.common.dto.UserDTO;

public class UserList
{
    private List<UserDTO> list;
    
    public UserList() {}
    
    public UserList(List<UserDTO> list)
    {
        this.list = list;
    }

    public List<UserDTO> getList()
    {
        return list;
    }

    public void setList(List<UserDTO> list)
    {
        this.list = list;
    }
}
