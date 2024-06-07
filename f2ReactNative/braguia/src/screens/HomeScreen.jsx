import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'

const HomeScreen = () => {

    const [title, setTitle] = useState("Title");
    const [appInfo, setAppInfo] = useState("Loading");
    const [appDesc, setAppDesc] = useState("Loading");
    const [trails, setTrails] = useState([]);

  
    const getTitle = async () => {
      try {
        const response = await fetch(api + 'app');
        if (response.ok) {
          const data = await response.json();
          setTitle(data[0].app_name);
          setAppInfo(data[0].app_desc);
          setAppDesc(data[0].app_landing_page_text);
          //console.log(data[0].app_landing_page_text);
        } else {
          setAppInfo("Error fetching app ifo");
        }
      } catch (error) {
        setAppInfo("Error fetching da!ta");
      }
    };

    const getTrails = async () => {
        try{
            const response = await fetch(api + 'trails');
            if (response.ok) {
                const data = await response.json();
                //console.log(data[0]);
                setTrails(data)
            } 
        }
        catch (error) {
            setTrails("Error fetching trails:", error);
        }
    }

    

  
    useEffect(() => {
      getTitle();
      getTrails();
    }, []);
    // <Image source={{ uri: item.image }} style={styles.trailImage} />

    const renderTrail = ({ item }) => (
        <View style={styles.trailItem}>
          <Image source={{ uri: item.trail_img }} style={styles.trailImage} />
          <Text style={styles.trailName}>{item.trail_name}</Text>
        </View>
      );


  
    return (
      <View style={styles.container}>
        <Text style={styles.title}>{title}</Text>
        <Text style={styles.title}>{appInfo}</Text>
        
        <FlatList
        data={trails}
        renderItem={renderTrail}
        keyExtractor={(item) => item.id.toString()}
        horizontal
        showsHorizontalScrollIndicator={false}
      />
        <Text style={styles.title}>{appDesc}</Text>
      </View>
    );
};

const styles = StyleSheet.create({
    button: {
      backgroundColor: cores.uminho, // Set your desired background color
      paddingHorizontal: 20,
      paddingVertical: 10,
      borderRadius: 5,
      elevation: 3, // Add shadow on Android
    },
    inputContainer: {
      flexDirection: 'row',
      alignItems: 'center',
      borderBottomWidth: 1,
      borderBottomColor: 'white',
      marginBottom: 10,
    },
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      padding: 20,
      backgroundColor : 'white',
  
    },
    image: {
      width: 200,  // Set the desired width
      height: 200, // Set the desired height
      marginBottom: 20,
      resizeMode: 'contain', // This ensures the image is scaled to fit the container
    },
    title: {
      fontSize: 24,
      fontWeight: 'bold',
      marginBottom: 55,
      color : 'black'
    },
    trailItem: {
        marginRight: 16,
        alignItems: 'center',
      },
      trailImage: {
        width: 100,
        height: 100,
        borderRadius: 8,
      },
      trailName: {
        marginTop: 8,
        fontSize: 16,
        color : 'black',
        fontWeight: 'bold',
      }
  });
export default HomeScreen;