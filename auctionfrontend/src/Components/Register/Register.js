import React, { useState } from "react";
import axios from "axios";

import { useNavigate, NavLink } from "react-router-dom";
const Register = () => {
  const navigate = useNavigate();

  const [formErrors, setFormErrors] = useState(false);
  const [user, setUserDetails] = useState({
    username: "",
    email: "",
    password: "",
    roles: [],
  });

  const changeHandler = (e) => {
    const { name, value } = e.target;
    setUserDetails((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const signupHandler = (e) => {
    e.preventDefault();
    const error = {};
    const regex = /^[^\s+@]+@[^\s@]+\.[^\s@]{2,}$/i;
    if (!user.username) {
      error.username = "User Name is required";
    }
    if (!user.email) {
      error.email = "Email is required";
    } else if (!regex.test(user.email)) {
      error.email = "This is not a valid email format!";
    }
    if (!user.password) {
      error.password = "Password is required";
    } else if (user.password.length < 4) {
      error.password = "Password must be more than 4 characters";
    } else if (user.password.length > 10) {
      error.password = "Password cannot exceed more than 10 characters";
    }
    if (user.roles.length === 0) {
      error.roles = "Role is not selected";
    }
    if (Object.keys(error).length === 0) {
      submitRequest();
    } else {
      setFormErrors(error);
    }
  };

  const submitRequest = () => {
    axios
      .post("http://localhost:8080/api/v1/addNewUser", user)
      .then((res) => {
        navigate("/login", { replace: true });
      })
      .catch((error) => {
        if (error.response.status === 403) {
          navigate("/login");
        }
      });
  };

  const handleRoleChange = (event) => {
    let selected = Array.from(
      event.target.selectedOptions,
      (option) => option.value
    );
    setUserDetails({ ...user, roles: selected });
  };

  return (
    <>
      <div
        className="container"
        style={{ paddingBottom: "10px", marginTop: "50px" }}
      >
        <form className="card-form">
          <h1>Sign Up</h1>
          <div className="form-group">
            <label htmlFor="username">User Name</label>
            <input
              type="text"
              name="username"
              id="username"
              placeholder="User Name"
              onChange={changeHandler}
              value={user.username}
              className="form-control"
            />
            <p className="text-danger">{formErrors.username}</p>
          </div>
          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="text"
              name="email"
              id="email"
              placeholder="Email"
              onChange={changeHandler}
              value={user.email}
              className="form-control"
            />
            <p className="text-danger">{formErrors.email}</p>
          </div>
          <div className="form-group">
            <label htmlFor="password">Password:</label>
            <input
              type="password"
              name="password"
              id="password"
              placeholder="Password"
              onChange={changeHandler}
              value={user.password}
              className="form-control"
            />
            <p className="text-danger">{formErrors.password}</p>
          </div>
          <div className="form-group">
            <label htmlFor="roles">Roles:</label>
            <select
              name="roles"
              id="roles"
              value={user.roles}
              onChange={(event) => handleRoleChange(event)}
              multiple
              className="form-select"
            >
              <option value="ADMIN">ADMIN</option>
              <option value="AUCTIONEER">AUCTIONEER</option>
              <option value="PARTICIPANT">PARTICIPANT</option>
            </select>
            <p className="text-danger">{formErrors.roles}</p>
          </div>
          <button className="btn btn-lg btn-primary" onClick={signupHandler}>
            Register
          </button>
          <br />
          <NavLink to="/login">Already registered? Login</NavLink>
        </form>
      </div>
    </>
  );
};
export default Register;
