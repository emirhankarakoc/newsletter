import axios from "axios";

const LOCALHOST = "http://localhost:8080";
export const APIURL = LOCALHOST;

const baseUrl = "http://localhost:5173";
export const CLIENTURL = baseUrl;
const token = localStorage.getItem("jwtToken");

export const http = axios.create({
  baseURL: APIURL,
  data: {},
  headers: token ? { Authorization: "Bearer " + token } : {},
});

export const httpError = (error: unknown) => {
  if (!axios.isAxiosError(error)) {
    return "Unknown error occured.";
  }

  if (!error.response) {
    return "Connection error.";
  }

  const errorMessage = error.response.data.message as string;

  return errorMessage;
};
