import axios from "axios";
import { toast } from "react-toastify";
import { ERROR_MESSAGES } from "../types/errors";
import myLoggerService from "../services/loggerService";

export const getApiErrorMessage = (error: unknown, context: string): string => {
  if (axios.isAxiosError(error) && typeof error.response?.data?.message === "string") {
    return error.response.data.message;
  }

  const message =
    error instanceof Error
      ? error.message
      : typeof error === "string"
        ? error
        : ERROR_MESSAGES.UNKNOWN_ERROR;

  myLoggerService.error(
    `Error in ${context}: ${message}`,
    error instanceof Error ? error : undefined
  );

  return message;
};

export const handleAndShowError = (error: unknown, context: string): string => {
  const errorMessage = getApiErrorMessage(error, context);
  toast.error(errorMessage);
  return errorMessage;
};

export const validateInput = (
  value: unknown,
  fieldName: string,
  required: boolean = true
): void => {
  if (required && (value === undefined || value === null || value === "")) {
    throw new Error(`${fieldName} alanı zorunludur`);
  }
};

export const validateCarId = (id: string): void => {
  if (!id || typeof id !== "string" || id.trim() === "") {
    throw new Error("Geçersiz araç ID formatı");
  }
};
