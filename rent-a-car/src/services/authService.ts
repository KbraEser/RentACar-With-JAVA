import { apiClient } from "../lib/apiClient";
import type { AppUser, AuthResponse, AuthSession } from "../types/auth";
import { getApiErrorMessage, validateInput } from "../utils/errorHandler";

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

interface UserProfileResponse {
  userId: number;
  email: string;
  name: string;
  surname: string;
}

const toAppUser = (profile: UserProfileResponse): AppUser => ({
  id: profile.userId,
  email: profile.email,
  name: profile.name,
  surname: profile.surname,
});

const toAuthResponse = (user: AppUser): AuthResponse => ({
  user,
  session: { user },
});

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

  return { user: null, session: null };
};

export const signIn = async (data: SignInData): Promise<AuthResponse> => {
  validateInput(data.email, "Email");
  validateInput(data.password, "Şifre");

  try {
    const { data: profile } = await apiClient.post<UserProfileResponse>(
      "/auth/login",
      {
        email: data.email,
        password: data.password,
      }
    );

    return toAuthResponse(toAppUser(profile));
  } catch (error) {
    throw new Error(getApiErrorMessage(error, "AuthService.signIn"));
  }
};

export const signOut = async (): Promise<void> => {
  try {
    await apiClient.post("/auth/logout");
  } catch {
    // Cookie temizliği backend'de; istemci state'i yine de sıfırlanır
  }
};

export const getSession = async (): Promise<{ session: AuthSession | null }> => {
  try {
    const { data: profile } = await apiClient.get<UserProfileResponse>("/auth/me");
    return { session: { user: toAppUser(profile) } };
  } catch {
    return { session: null };
  }
};

export const getUser = async (): Promise<AppUser | null> => {
  const { session } = await getSession();
  return session?.user ?? null;
};
