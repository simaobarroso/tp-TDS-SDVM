import { StyleSheet, Text, View } from 'react-native'
import React from 'react'

const Trail = () => {
    const [currentTheme, setCurrentTheme] = useState(theme);

  const toggleTheme = () => {
    setCurrentTheme(currentTheme === 'light' ? 'dark' : 'light');
  };

  const currentColorScheme = currentTheme === 'light' ? light : dark;

  return (
    <View>
      <Text>Trail</Text>
    </View>
  )
}


const styles = StyleSheet.create({})
export default Trail