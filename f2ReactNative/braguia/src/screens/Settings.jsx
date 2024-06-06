import React, { useState } from 'react';
import { SafeAreaView,Switch, View, Text, StyleSheet, Button, TouchableOpacity } from 'react-native';
import { cores, api, theme, dark, light } from '../var.js';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

const Settings = () => {
  const [isSwitch1Enabled, setIsSwitch1Enabled] = useState(true);
  const [isSwitch2Enabled, setIsSwitch2Enabled] = useState(false);

  const toggleSwitch1 = () => setIsSwitch1Enabled(previousState => !previousState);
  const toggleSwitch2 = () => setIsSwitch2Enabled(previousState => !previousState);

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.switchContainer}>
        <Text>Background Services</Text>
        <Switch
          trackColor={{ false: "#767577", true: "#81b0ff" }}
          thumbColor={isSwitch1Enabled ? "#f5dd4b" : "#f4f3f4"}
          ios_backgroundColor="#3e3e3e"
          onValueChange={toggleSwitch1}
          value={isSwitch1Enabled}
        />
      </View>
      <View style={styles.switchContainer}>
        <Text>Dark Theme</Text>
        <Switch
          trackColor={{ false: "#767577", true: "#81b0ff" }}
          thumbColor={isSwitch2Enabled ? "#f5dd4b" : "#f4f3f4"}
          ios_backgroundColor="#3e3e3e"
          onValueChange={toggleSwitch2}
          value={isSwitch2Enabled}
        />
      </View>
    </SafeAreaView>
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
