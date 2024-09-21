import { Button } from "@nextui-org/button";
import {
  Navbar,
  NavbarBrand,
  NavbarContent,
  NavbarItem,
} from "@nextui-org/navbar";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";

export default function Navigation() {
  const [isLoggedIn, setLoggedIn] = useState(false);
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    const handleToken = () => {
      const token = localStorage.getItem("jwtToken");
      if (token != null) {
        setLoggedIn(true);
      }
    };

    const handleIsAdmin = () => {
      const role = localStorage.getItem("role");
      console.log(role);

      if (role === "ROLE_ADMIN") {
        setIsAdmin(true);
      } else {
        setIsAdmin(false);
      }
    };

    handleIsAdmin();
    handleToken();
  }, [isLoggedIn]);

  const handleLogOut = () => {
    localStorage.removeItem("jwtToken");
    localStorage.removeItem("role");
    setLoggedIn(false);
    window.location.href = "/";
  };

  return (
    <Navbar>
      <NavbarBrand>
        <button
          className="font-bold text-inherit"
          onClick={() => {
            window.location.href = "/";
          }}
        >
          e-NEWSLETTER
        </button>
      </NavbarBrand>
      <NavbarContent className="hidden sm:flex gap-4" justify="center">
        <NavbarItem>
          <Link to="/" className="text-foreground">
            Home
          </Link>
        </NavbarItem>
        <NavbarItem isActive>
          <Link to="/help" aria-current="page">
            Help
          </Link>
        </NavbarItem>
        <NavbarItem>
          <Link to="/faq" className="text-foreground">
            FAQ
          </Link>
        </NavbarItem>
        {isAdmin && (
          <NavbarItem isActive>
            <Link to="/dashboard/admin" className="text-foreground">
              Dashboard
            </Link>
          </NavbarItem>
        )}
        {!isAdmin && (
          <NavbarItem isActive>
            <Link to="/dashboard" className="text-foreground">
              Dashboard
            </Link>
          </NavbarItem>
        )}
      </NavbarContent>
      {!isLoggedIn && (
        <NavbarContent justify="end">
          <NavbarItem className="hidden lg:flex">
            <Link to="/login">Login</Link>
          </NavbarItem>
          <NavbarItem>
            <Button as={Link} to="/register" color="primary" variant="flat">
              Sign Up
            </Button>
          </NavbarItem>
        </NavbarContent>
      )}
      {isLoggedIn && (
        <NavbarContent justify="end">
          <NavbarItem className="hidden lg:flex">
            <Button as={Link} to="/profile" color="secondary" variant="flat">
              Profile
            </Button>
          </NavbarItem>
          <NavbarItem>
            <Button onPress={handleLogOut} color="danger" variant="flat">
              Logout
            </Button>
          </NavbarItem>
        </NavbarContent>
      )}
    </Navbar>
  );
}
