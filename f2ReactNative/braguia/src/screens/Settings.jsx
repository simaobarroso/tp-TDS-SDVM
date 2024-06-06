import React, { useState } from 'react';
import { View, Text, StyleSheet, Button, TouchableOpacity } from 'react-native';
import { cores, api, theme, dark, light } from '../var.js';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

const Settings = () => {
  const [currentTheme, setCurrentTheme] = useState(theme);

  const toggleTheme = () => {
    setCurrentTheme(currentTheme === 'light' ? 'dark' : 'light');
  };

  const currentColorScheme = currentTheme === 'light' ? light : dark;

  return (
    <View style={[styles.container, { backgroundColor: currentColorScheme.back }]}>
      <Text style={[styles.text, { color: currentColorScheme.text }]}>Settings</Text>
      <TouchableOpacity style={styles.button} onPress={toggleTheme}>
        <Text style={[styles.buttonText, { color: currentColorScheme.text }]}>
          Toggle Theme
        </Text>
      </TouchableOpacity>
      {/* Add other components here */}
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
  },
  text: {
    fontSize: 18,
    marginBottom: 20,
  },
  button: {
    backgroundColor: '#007BFF',
    padding: 10,
    borderRadius: 5,
  },
  buttonText: {
    fontSize: 16,
  },
});

export default Settings;
