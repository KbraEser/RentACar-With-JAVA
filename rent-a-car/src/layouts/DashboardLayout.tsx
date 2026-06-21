import { useAppDispatch, useAppSelector } from "../app/hooks/storeHooks";
import { signOut } from "../store/slices/authSlice";
import { useState } from "react";
import type { RootState } from "../store/store";
import { Link } from "react-router-dom";

const DashboardLayout = () => {
  const dispatch = useAppDispatch();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const user = useAppSelector((state: RootState) => state.auth.user);
  return (
    <div className=" flex flex-col ">
      {/* Navbar */}
      <nav className="navbar">
        <div className="navbar-container">
          <div className="navbar-logo">CarRental</div>

          {/* Mobile Menu Button */}
          <button
            onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
            className="md:hidden text-white p-2"
          >
            <svg
              className="w-6 h-6"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              {mobileMenuOpen ? (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M6 18L18 6M6 6l12 12"
                />
              ) : (
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M4 6h16M4 12h16M4 18h16"
                />
              )}
            </svg>
          </button>

          {/* Desktop Menu */}
          <div className="hidden md:flex items-center gap-6">
            <ul className="navbar-links">
              <li>
                <Link to="/dashboard" className="navbar-link">
                  Dashboard
                </Link>
              </li>
              <li>
                <Link to="/dashboard/cars" className="navbar-link">
                  Araçlar
                </Link>
              </li>

              <li>
                <Link to="/dashboard/profile" className="navbar-link">
                  Profil
                </Link>
              </li>
            </ul>
            <div className="flex items-center gap-4">
              <span className="navbar-link hidden lg:block">
                Hoş geldin, {user?.name}
              </span>
              <button
                onClick={() => dispatch(signOut())}
                className="primary-button"
              >
                Çıkış Yap
              </button>
            </div>
          </div>
        </div>

        {/* Mobile Menu */}
        {mobileMenuOpen && (
          <div className="md:hidden bg-gray-800 border-t border-gray-700">
            <div className="px-6 py-4 space-y-4">
              <Link to="/dashboard/cars" className="block navbar-link py-2">
                Araçlar
              </Link>
              <Link
                to="/dashboard/reservation"
                className="block navbar-link py-2"
              >
                Rezervasyonlarım
              </Link>
              <Link to="/dashboard/profile" className="block navbar-link py-2">
                Profil
              </Link>
              <div className="pt-4 border-t border-gray-700">
                <div className="flex items-center justify-between">
                  <span className="navbar-link">
                    Hoş geldin, {user?.name}
                  </span>
                  <button
                    onClick={() => dispatch(signOut())}
                    className="primary-button"
                  >
                    Çıkış Yap
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </nav>
    </div>
  );
};

export default DashboardLayout;
