import React, { useState } from "react";
import axios from "axios";
import { useNavigate, NavLink } from "react-router-dom";
const Login = () => {

  const navigate = useNavigate();
  const [formErrors, setFormErrors] = useState(false);

  const [user, setUserDetails] = useState({
    username: "",
    password: "",
  });

  const usernameChangeHandler = (e) => {

    setUserDetails({
      username: e.target.value,
      password: user.password,
    });
  };

  const passwordChangeHandler = (e) => {
    setUserDetails({
      username: user.username,
      password: e.target.value,
    });
  };

  const loginHandler = (e) => {
    e.preventDefault();
    axios.post("http://localhost:8080/api/v1/login", user).then((res) => {
      const error={};
      if(res.status===200){
        const roles=res.data.roles;
        localStorage.setItem("token", res.data.token);
        localStorage.setItem("roles",JSON.stringify(roles));
        if(roles.includes("AUCTIONEER")){
           navigate("/auction/list", { replace: true });
        }else if(roles.includes("PARTICIPANT")){
          navigate("/auction/bidlist", { replace: true }); 
        }else if(roles.includes("ADMIN")){
          navigate("/report", { replace: true });
        }
      }else{
        error.login="UserName or Password!!!";
        setFormErrors(error);
      }
    
    }).catch(e=>{
      const error={}
      error.login="Invalid UserName or Password!!!";
      setFormErrors(error);
    });
  };


  return (
    <div className="container">
      <form className="card-form" style={{width:'40%',marginLeft:'350px'}}>
        <h1>Login</h1>
        <div className="form-group">
        <input
          type="text"
          name="username"
          id="username"
          className="form-control"
          placeholder="UserName"
          onChange={usernameChangeHandler}
          value={user.username}
        />
        </div>
        <br/>
        <div className="form-group">
        <input
          type="password"
          name="password"
          id="password"
          className="form-control"
          placeholder="Password"
          onChange={passwordChangeHandler}
          value={user.password}
        />
        </div>
        <p className="text-danger">{formErrors.login}</p>
        <br/>
        <div class="row justify-content-center ">
        <button className="btn btn-primary  w-50" onClick={loginHandler}>
          Login
        </button>
        <br/>
        <NavLink to="/signup" style={{width:'70%'}} >Not yet registered? Register Now</NavLink>
        </div>
      </form>
      
    </div>
  );
};
export default Login;
