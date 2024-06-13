import React, {useContext} from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { useSelector } from 'react-redux';
import Icon from 'react-native-vector-icons/EvilIcons';


const User = () => {

    const userMetaData = useSelector((state) => state.data.user.username);
    const {
      username,
      email,
      date_joined,
      user_type,
    } = userMetaData;
    
    console.log(userMetaData);
  
    const formattedDateJoined = new Date(date_joined).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
      });

      return (
        <View style={[styles.container, ]}>
          <View style={styles.content}>
            <View style={styles.profileContainer}>
              <View style={styles.backgroundContainer}>
              <Icon size={20} color="white" name="user" />
                <Text style={[styles.username, {color:'black'}]}>{username}</Text>
                <Text style={[styles.infoTitle, {color:'black'}]}>User type</Text>
                <Text style={[styles.userType, {color:'black'}]}>{user_type}</Text>
              </View>
            </View>
    
            <View style={styles.detailContainer}>
              <View style={styles.detailRow}>
                <Text style={[styles.detailText, {color:'black'}]}>{username}</Text>
              </View>
              <View style={styles.detailRow}>

              </View>
              <View style={styles.detailRow}>
                <Text style={[styles.detailText, {color:'black'}]}>
                  Member since {formattedDateJoined}
                </Text>
              </View>
            </View>
    
            <TouchableOpacity style={styles.button} >
              <Text style={styles.buttonText}>Trails history</Text>
            </TouchableOpacity>
          </View>
        </View>
      );

}

const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'space-between',
      alignItems: 'center',
    },
    content: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
    },
    profileContainer: {
      height: 350,
      width: 350,
      marginBottom: 20,
    },
    backgroundContainer: {
      flex: 1,
      backgroundColor: '#FF0008',
      justifyContent: 'center',
      alignItems: 'center',
    },
    username: {
      marginTop: 10,
      color: '#ffffff',
      fontSize: 21,
      fontWeight: 'bold',
    },
    infoTitle: {
      fontSize: 20,
      fontWeight: 'bold',
      color: '#000000',
      marginTop: 10,
    },
    userType: {
      color: '#FFFFFF',
      fontSize: 20,
      fontWeight: 'bold',
    },
    detailContainer: {
      marginBottom: 20,
    },
    detailRow: {
      flexDirection: 'row',
      alignItems: 'center',
      marginBottom: 15,
    },
    detailText: {
      paddingLeft: 20,
      fontWeight: 'bold',
      fontSize: 16,
      color: '#000000',
    },
    button: {
      flexDirection: 'row',
      justifyContent: 'center',
      alignItems: 'center',
      backgroundColor: '#FF0008',
      paddingHorizontal: 10,
      paddingVertical: 10,
      borderRadius: 10,
      width: '50%', // Adjust the width as per your requirements
    },
    buttonText: {
      color: '#FFFFFF',
      fontWeight: 'bold',
      fontSize: 18,
      marginLeft: 5,
      marginRight: 10,
    },
  });

export default User;

