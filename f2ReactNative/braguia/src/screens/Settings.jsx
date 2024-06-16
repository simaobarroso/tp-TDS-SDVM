import React, { useState } from 'react';
import { SafeAreaView,Switch, View, Text, StyleSheet, Button, TouchableOpacity } from 'react-native';
import { cores, api, theme, dark, light } from '../var.js';
import Icon from 'react-native-vector-icons/MaterialCommunityIcons';

const Settings = () => {
  const [isSwitch1Enabled, setIsSwitch1Enabled] = useState(true);


  const toggleSwitch1 = () => setIsSwitch1Enabled(previousState => !previousState);


  return (
    <SafeAreaView style={styles.container}>
      <Text style={styles.title}>Settings</Text>
      <View style={styles.containerSetting}>
        <Text style={styles.text}>Location Services</Text>
        <Switch
          trackColor={{ false: "#767577", true: "#854e4e" }}
          thumbColor={cores.uminho}
          ios_backgroundColor="#3e3e3e"
          onValueChange={toggleSwitch1}
          value={isSwitch1Enabled}
        />
        </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    margin: 15
  },
  title: {
    fontSize: 24,
    fontWeight: 'bold',
    marginTop: 15,
    marginBottom: 250,
    color : cores.uminho
  },
  containerSetting: {
    flexDirection: "row"
  },
  text: {
    fontSize: 18,
    marginBottom: 20,
    color: "black"
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
