import { NativeModules } from 'react-native';

const { ZohoAuth } = NativeModules;

export type ZohoAppKey = 'app1' | 'app2';

export const zohoAuth = {
  login: (appKey: ZohoAppKey): Promise<boolean> => {
    return ZohoAuth.login(appKey);
  },

  isLoggedIn: (): Promise<boolean> => {
    return ZohoAuth.isLoggedIn();
  },

  logout: (): Promise<boolean> => {
    return ZohoAuth.logout();
  },
};

export default zohoAuth;