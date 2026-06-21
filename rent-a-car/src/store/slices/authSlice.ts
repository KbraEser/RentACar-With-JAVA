import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import {
  signUp as signUpService,
  signIn as signInService,
  signOut as signOutService,
  getSession as getSessionService,
} from "../../services/authService";
import type { AppUser, AuthSession } from "../../types/auth";
import { ERROR_MESSAGES } from "../../types/errors";

type AuthState = {
  user: AppUser | null;
  session: AuthSession | null;
  loading: boolean;
  error: string | null;
};

const initialState: AuthState = {
  user: null,
  session: null,
  loading: false,
  error: null,
};

// Async thunks
export const signUp = createAsyncThunk(
  "auth/signUp",
  async (data: {
    email: string;
    password: string;
    name: string;
    surname: string;
    passwordConfirm: string;
  }) => {
    return await signUpService(data);
  }
);

export const signIn = createAsyncThunk(
  "auth/signIn",
  async (data: { email: string; password: string }) => {
    return await signInService(data);
  }
);

export const signOut = createAsyncThunk("auth/signOut", async () => {
  return await signOutService();
});

export const getSession = createAsyncThunk("auth/getSession", async () => {
  return await getSessionService();
});

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setUser: (state, action) => {
      state.user = action.payload;
    },
    setSession: (state, action) => {
      state.session = action.payload;
    },
    clearError: (state) => {
      state.error = null;
    },
  },
  extraReducers: (builder) => {
    builder
      // Sign Up
      .addCase(signUp.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(signUp.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.user;
        state.session = action.payload.session;
      })
      .addCase(signUp.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || ERROR_MESSAGES.SIGNUP_FAILED;
      })
      // Sign In
      .addCase(signIn.pending, (state) => {
        state.loading = true;
        state.error = null;
      })
      .addCase(signIn.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.user;
        state.session = action.payload.session;
      })
      .addCase(signIn.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || ERROR_MESSAGES.SIGNIN_FAILED;
      })
      // Sign Out
      .addCase(signOut.pending, (state) => {
        state.loading = true;
      })
      .addCase(signOut.fulfilled, (state) => {
        state.loading = false;
        state.user = null;
        state.session = null;
      })
      .addCase(signOut.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || "Çıkış işlemi başarısız";
      })
      // Get Session
      .addCase(getSession.pending, (state) => {
        state.loading = true;
      })
      .addCase(getSession.fulfilled, (state, action) => {
        state.loading = false;
        state.user = action.payload.session?.user || null;
        state.session = action.payload.session;
      })
      .addCase(getSession.rejected, (state, action) => {
        state.loading = false;
        state.error = action.error.message || "Oturum bilgisi alınamadı";
      });
  },
});

export const { setUser, setSession, clearError } = authSlice.actions;
export default authSlice.reducer;
