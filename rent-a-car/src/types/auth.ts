export type AppUser = {
  id: number;
  email: string;
  name: string;
  surname?: string;
};

export type AuthSession = {
  user: AppUser;
};

export type AuthResponse = {
  user: AppUser | null;
  session: AuthSession | null;
};
