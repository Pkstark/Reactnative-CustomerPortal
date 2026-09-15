import React, { useState } from 'react';
import {
  Alert,
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

import ZohoAuth, { ZohoAppKey } from '../native/zohoAuth';

type HomeScreenProps = {
  selectedApp: ZohoAppKey | null;
  onLogout: () => void;
};

const HomeScreen = ({
  selectedApp,
  onLogout,
}: HomeScreenProps) => {

  const [loading, setLoading] = useState(false);

  const handleLogout = async () => {

    try {

      setLoading(true);

      const success = await ZohoAuth.logout();

      if (success) {
        onLogout();
      }

    } catch (error: any) {

      Alert.alert(
        'Logout Failed',
        error?.message || 'Unable to logout.'
      );

    } finally {

      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>

      <View style={styles.content}>

        <Text style={styles.title}>
          Home
        </Text>

        <Text style={styles.message}>
          Customer Portal login successful.
        </Text>

        <Text style={styles.app}>
          Selected Application: {selectedApp}
        </Text>

        <TouchableOpacity
          style={styles.logoutButton}
          onPress={handleLogout}
          disabled={loading}
        >
          <Text style={styles.logoutText}>
            {loading ? 'Logging out...' : 'Logout'}
          </Text>
        </TouchableOpacity>

      </View>

    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
  },

  content: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    paddingHorizontal: 24,
  },

  title: {
    fontSize: 28,
    fontWeight: '700',
    marginBottom: 16,
  },

  message: {
    fontSize: 18,
    textAlign: 'center',
    marginBottom: 12,
  },

  app: {
    fontSize: 16,
    marginBottom: 24,
  },

  logoutButton: {
    borderRadius: 8,
    paddingVertical: 14,
    paddingHorizontal: 30,
  },

  logoutText: {
    fontSize: 16,
    fontWeight: '700',
  },
});

export default HomeScreen;