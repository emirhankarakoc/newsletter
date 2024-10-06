interface User {
  id: string;
  email: string;
  role: string;
  oauth2: OAuth2Account;
  userStatus: UserStatus;
}

type UserStatus = "GOOGLE_VERIFICATED" | "GOOGLE_NOT_VERIFICATED";

interface OAuth2Account {
  id: string;
  email: string;
  family_name: string;
  given_name: string;
  gmailId: string;
  name: string;
  picture: string;
  verified_email: boolean;
  access_token: string;
}
interface Newsletter {
  id: string;
  name: string;
  ownerUserId: string;
  description: string;
  imageUrl: string;
  imageId: string;
  customers: Customer[];
}
interface Customer {
  id: string;
  name: string;
  email: string;
  registerDate: string;
}

interface MailPreview {
  id: string;
  name: string;
  createdDate: string;
  updatedDate: string;
  htmlContent: string;
  ownerId: string;
}
