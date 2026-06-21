import {
  apiClient,
  clearToken,
  getToken,
  setToken,
} from "../lib/apiClient";
import type { AppUser, AuthResponse, AuthSession } from "../types/auth";
import { getApiErrorMessage, validateInput } from "../utils/errorHandler";

const USER_KEY = "auth_user";

export interface SignUpData {
  email: string;
  password: string;
  name: string;
  surname: string;
  passwordConfirm: string;
}

export interface SignInData {
  email: string;
  password: string;
}

interface LoginResponse {
  jwtToken: string;
  userId: number;
  email: string;
  name: string;
  surname: string;
}

const saveAuth = (token: string, user: AppUser): AuthResponse => {
  setToken(token);
  localStorage.setItem(USER_KEY, JSON.stringify(user));

  const session: AuthSession = { access_token: token, user };
  return { user, session };
};

const getStoredUser = (): AppUser | null => {
  const userJson = localStorage.getItem(USER_KEY);
  if (!userJson) return null;

  try {
    return JSON.parse(userJson) as AppUser;
  } catch {
    return null;
  }
};

export const signUp = async (data: SignUpData): Promise<AuthResponse> => {
  validateInput(data.email, "Email");
  validateInput(data.password, "Şifre");
  validateInput(data.name, "Ad");
  validateInput(data.surname, "Soyad");
  validateInput(data.passwordConfirm, "Şifre tekrarı");

  try {
    await apiClient.post("/auth/register", {
      name: data.name,
      surname: data.surname,
      email: data.email,
      password: data.password,
      passwordConfirm: data.passwordConfirm,
    });
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "AuthService.signUp"));
  }

  // Backend kayıtta token dönmüyor; kullanıcı login sayfasına gider
  return { user: null, session: null };
};

export const signIn = async (data: SignInData): Promise<AuthResponse> => {
  validateInput(data.email, "Email");
  validateInput(data.password, "Şifre");

  try {
    const { data: loginData } = await apiClient.post<LoginResponse>(
      "/auth/login",
      {
        email: data.email,
        password: data.password,
      }
    );

    const user: AppUser = {
      id: loginData.userId,
      email: loginData.email,
      name: loginData.name,
      surname: loginData.surname,
    };

    return saveAuth(loginData.jwtToken, user);
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "AuthService.signIn"));
  }
};

export const signOut = async (): Promise<void> => {
  clearToken();
  localStorage.removeItem(USER_KEY);
};

export const getSession = async (): Promise<{ session: AuthSession | null }> => {
  const token = getToken();
  const user = getStoredUser();

  if (!token || !user) {
    return { session: null };
  }

  return { session: { access_token: token, user } };
};

export const getUser = async (): Promise<AppUser | null> => {
  const { session } = await getSession();
  return session?.user ?? null;
};
