package aic12.project3.dao;

import java.util.List;

import aic12.project3.dto.UserDTO;

public interface IUserDAO {
	public void storeUser(UserDTO user);
	public void storeUser(List<UserDTO> users);
	public List<UserDTO> searchUser(String userName);
	public List<UserDTO> getAllUser(String userName);
	
}
