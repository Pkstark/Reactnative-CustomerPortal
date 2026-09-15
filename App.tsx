import React, { useEffect, useState } from 'react';
import {
  ActivityIndicator,
  SafeAreaView,
  StyleSheet,
  Text,
} from 'react-native';

import ZohoAuth, { ZohoAppKey } from './src/native/zohoAuth';
import LoginScreen from './src/components/LoginScreen';
import HomeScreen from './src/components/HomeScreen';

const App = () => {

  const [loading, setLoading] = useState(true);
  const [loggedIn, setLoggedIn] = useState(false);
  const [selectedApp, setSelectedApp] = useState<ZohoAppKey | null>(null);

  useEffect(() => {

    const checkLogin = async () => {

      try {

        const isLoggedIn = await ZohoAuth.isLoggedIn();

        setLoggedIn(isLoggedIn);

      } catch (error) {

        setLoggedIn(false);

      } finally {

        setLoading(false);
      }
    };

    checkLogin();

  }, []);

  const handleLoginSuccess = (appKey: ZohoAppKey) => {

    setSelectedApp(appKey);
    setLoggedIn(true);
  };

  const handleLogout = () => {

    setSelectedApp(null);
    setLoggedIn(false);
  };

  if (loading) {

    return (
      <SafeAreaView style={styles.loadingContainer}>
        <ActivityIndicator />
      </SafeAreaView>
    );
  }

  if (!loggedIn) {

    return (
      <LoginScreen
        onLoginSuccess={handleLoginSuccess}
      />
    );
  }

  return (
    <HomeScreen
      selectedApp={selectedApp}
      onLogout={handleLogout}
    />
  );
};

const styles = StyleSheet.create({
  loadingContainer: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
});

export default App;