import axios, { type InternalAxiosRequestConfig } from "axios";

const API_URL = import.meta.env.VITE_API_URL as string;

const CSRF_COOKIE_NAME = "XSRF-TOKEN";
const CSRF_HEADER_NAME = "X-XSRF-TOKEN";

const getCsrfToken = (): string | null => {
  const match = document.cookie.match(
    new RegExp(`(?:^|; )${CSRF_COOKIE_NAME}=([^;]*)`)
  );
  return match ? decodeURIComponent(match[1]) : null;
};

const isAuthEndpoint = (url?: string): boolean => {
  if (!url) return false;
  return (
    url.includes("/auth/login") ||
    url.includes("/auth/register") ||
    url.includes("/auth/refresh") ||
    url.includes("/auth/logout") ||
    url.includes("/auth/me")
  );
};

export const apiClient = axios.create({
  baseURL: API_URL,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.request.use((config: InternalAxiosRequestConfig) => {
  const csrfToken = getCsrfToken();
  if (csrfToken) {
    config.headers[CSRF_HEADER_NAME] = csrfToken;
  }
  return config;
});

let refreshPromise: Promise<void> | null = null;

const refreshSession = (): Promise<void> => {
  if (!refreshPromise) {
    refreshPromise = apiClient
      .post("/auth/refresh")
      .then(() => undefined)
      .finally(() => {
        refreshPromise = null;
      });
  }
  return refreshPromise;
};

apiClient.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config as InternalAxiosRequestConfig & {
      _retry?: boolean;
    };

    if (
      error.response?.status === 401 &&
      originalRequest &&
      !originalRequest._retry &&
      !isAuthEndpoint(originalRequest.url)
    ) {
      originalRequest._retry = true;

      try {
        await refreshSession();
        return apiClient(originalRequest);
      } catch {
        if (!window.location.pathname.startsWith("/auth/")) {
          window.location.href = "/auth/login";
        }
      }
    }

    return Promise.reject(error);
  }
);
