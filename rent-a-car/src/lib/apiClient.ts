import axios from "axios";


const API_URL = import.meta.env.VITE_API_URL as string;

const TOKEN_KEY ="auth_token";

export const getToken =() : string |null => localStorage.getItem(TOKEN_KEY);

export const setToken = (token: string): void => {
  localStorage.setItem(TOKEN_KEY, token);
};

export const clearToken = (): void =>{
  localStorage.removeItem(TOKEN_KEY);
};

export const apiClient = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
})

apiClient.interceptors.request.use((config)=>{
  const token = getToken();
  if(token){
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
})


apiClient.interceptors.response.use((response)=>
 response,

 (error)=>{
  if(error.response?.status === 401){
    localStorage.removeItem(TOKEN_KEY);
    window.location.href = "/auth/login";
  }
  return Promise.reject(error);
 }
)

