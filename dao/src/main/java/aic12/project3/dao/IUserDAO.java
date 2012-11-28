package aic12.project3.dao;

import java.util.List;

import aic12.project3.common.dto.UserDTO;

public interface IUserDAO {
	public abstract void storeUser(UserDTO user);
	public abstract void storeUser(List<UserDTO> users);
	public abstract UserDTO searchUser(String userName);
	public abstract List<UserDTO> getAllUser();

}