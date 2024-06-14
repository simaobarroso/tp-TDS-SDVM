import { View, Text,TextInput, Image  } from 'react-native';
import { StyleSheet, FlatList} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'

/* To-DO:
- Melhorar interface
- Fazer redirects
- Dark Mode !
*/

const Search = () => {
  const [title, setTitle] = useState("Title");

  const [appInfo, setAppInfo] = useState("Loading");

  const [appDesc, setAppDesc] = useState("Loading");

  const trails  = useSelector(state => state.data.appData.trails);
  const appinfo = useSelector(state => state.data.appData.appinfo);

  const [searchQuery, setSearchQuery] = useState('');
  const [filteredTrails, setFilteredTrails] = useState(trails);


  const updateInfo = async () => {
      setTitle(appinfo.app_name);
      setAppInfo(appinfo.app_desc);
      setAppDesc(appinfo.app_landing_page_text);
      //console.log(data[0].app_landing_page_text);

  };
  // FIXME remover isto
  //const getTrails = async () => {
  //    try{
  //        const response = await fetch(api + 'trails');
  //        if (response.ok) {
  //            const data = await response.json();
  //            //console.log(data[0]);
  //            setTrails(data)
  //        }
  //    }
  //    catch (error) {
  //        setTrails("Error fetching trails:", error);
  //    }
  //}


  useEffect(() => {
    updateInfo();
  }, []);
  // <Image source={{ uri: item.image }} style={styles.trailImage} />

  useEffect(() => {
      filterTrails(searchQuery);
    }, [searchQuery, trails]);
  
    const filterTrails = (query) => {
      if (query) {
        const filtered = trails.filter((trail) =>
          trail.trail_name.toLowerCase().includes(query.toLowerCase())
        );
        setFilteredTrails(filtered);
      } else {
        setFilteredTrails(trails);
      }
    };



    const renderTrail = ({ item }) => (
        <View style={styles.trailItem}>
          <Image source={{ uri: item.trail_img }} style={styles.trailImage} />
          <Text style={styles.trailName}>{item.trail_name}</Text>
        </View>
      );


  
    return (
      <View style={styles.container}>

    <TextInput
        style={styles.input}
        placeholder="Search by trail name"
        value={searchQuery}
        onChangeText={setSearchQuery}
      />

        
        <FlatList
        data={filteredTrails}
        renderItem={renderTrail}
        keyExtractor={(item) => item.id.toString()}
        vertical
        showsHorizontalScrollIndicator={false}
      />
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
    input: {
        width: '80%',
        height: 40,
        borderColor: 'gray',
        borderWidth: 1,
        marginBottom: 20,
        paddingHorizontal: 20,
        color : 'black',
        borderRadius: 5,
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

export default Search