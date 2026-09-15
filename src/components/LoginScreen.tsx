import React, { useState } from 'react';
import {
  SafeAreaView,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
  Alert,
} from 'react-native';

import ZohoAuth, { ZohoAppKey } from '../native/zohoAuth';

type LoginScreenProps = {
  onLoginSuccess: (appKey: ZohoAppKey) => void;
};

const apps: {
  key: ZohoAppKey;
  name: string;
}[] = [
  {
    key: 'app1',
    name: 'TrackMyTime',
  },
  {
    key: 'app2',
    name: 'Testing App',
  },
];

const LoginScreen = ({
  onLoginSuccess,
}: LoginScreenProps) => {

  const [selectedApp, setSelectedApp] = useState<ZohoAppKey>('app1');
  const [loading, setLoading] = useState(false);

  const handleLogin = async () => {

    try {

      setLoading(true);

      const success = await ZohoAuth.login(selectedApp);

      if (success) {
        onLoginSuccess(selectedApp);
      }

    } catch (error: any) {

      Alert.alert(
        'Login Failed',
        error?.message || 'Unable to login.'
      );

    } finally {

      setLoading(false);
    }
  };

  return (
    <SafeAreaView style={styles.container}>

      <View style={styles.content}>

        <Text style={styles.title}>
          Customer Portal Login
        </Text>

        <Text style={styles.subtitle}>
          Select Creator Application
        </Text>

        <View style={styles.appContainer}>

          {apps.map((app) => {

            const isSelected = selectedApp === app.key;

            return (
              <TouchableOpacity
                key={app.key}
                style={[
                  styles.appButton,
                  isSelected && styles.selectedAppButton,
                ]}
                onPress={() => setSelectedApp(app.key)}
                disabled={loading}
              >
                <Text
                  style={[
                    styles.appButtonText,
                    isSelected && styles.selectedAppButtonText,
                  ]}
                >
                  {app.name}
                </Text>
              </TouchableOpacity>
            );
          })}

        </View>

        <TouchableOpacity
          style={styles.loginButton}
          onPress={handleLogin}
          disabled={loading}
        >
          <Text style={styles.loginButtonText}>
            {loading ? 'Logging in...' : 'Login'}
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
    paddingHorizontal: 24,
  },

  title: {
    fontSize: 26,
    fontWeight: '700',
    textAlign: 'center',
    marginBottom: 10,
  },

  subtitle: {
    fontSize: 16,
    textAlign: 'center',
    marginBottom: 24,
  },

  appContainer: {
    gap: 12,
    marginBottom: 24,
  },

  appButton: {
    borderWidth: 1,
    borderRadius: 8,
    paddingVertical: 16,
    paddingHorizontal: 16,
  },

  selectedAppButton: {
    borderWidth: 2,
  },

  appButtonText: {
    fontSize: 16,
    textAlign: 'center',
  },

  selectedAppButtonText: {
    fontWeight: '700',
  },

  loginButton: {
    borderRadius: 8,
    paddingVertical: 16,
    alignItems: 'center',
  },

  loginButtonText: {
    fontSize: 16,
    fontWeight: '700',
  },
});

export default LoginScreen;