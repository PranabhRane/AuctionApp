import axios from "axios";
import { useNavigate } from "react-router-dom";

const api = axios.create({
  baseURL: "http://localhost:8080/api/v1",
});

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    if (error.response.status === 401) {
      //place your reentry code
    }
    if (error.response.status === 401) {
      //place your reentry code
    }
    if (error.response.status === 403) {
      console.log("hi");
    }
    return Promise.reject(error);
  }
);

export default api;
