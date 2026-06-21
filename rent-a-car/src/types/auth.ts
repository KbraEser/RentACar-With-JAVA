export type AppUser = {
  id: number;
  email: string;
  name: string;
  surname?: string;
};

export type AuthSession = {
  access_token: string;
  user: AppUser;
};

export type AuthResponse = {
  user: AppUser | null;
  session: AuthSession | null;
};
