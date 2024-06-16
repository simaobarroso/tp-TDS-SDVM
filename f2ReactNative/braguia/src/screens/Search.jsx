import { View, Text,TextInput, Image  } from 'react-native';
import { StyleSheet, FlatList,  KeyboardAvoidingView,  TouchableOpacity, SafeAreaView, Alert } from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import { useNavigation } from '@react-navigation/native';
import {cores, api, api2} from '../var.js'
import Icon from 'react-native-vector-icons/MaterialIcons';
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

  const userType= useSelector((state) => state.data.user.user_type);

  const navigation = useNavigation();


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
      <TouchableOpacity
          style={styles.trailItem}
          onPress={() => { 
            if (userType === 'Premium') {
              navigation.navigate('Trail', { trail_id: item.id });
            } else {
              Alert.alert('Access Denied', 'Only premium users can access this feature');
            }
          }}
      >
          <Image source={{ uri: item.trail_img }} style={styles.trailImage} />
          <Text style={styles.trailName}>{item.trail_name}</Text>
      </TouchableOpacity>
  );


  return (
    <KeyboardAvoidingView style={{ flex: 1 }} behavior="padding">
      <SafeAreaView style={styles.container}>
        <View style={styles.topBar}>
          <View style={styles.inputContainer}>
            <Icon size={23} color="black" name="search" />
            <TextInput
                style={styles.input}
                placeholder="Search by trail name"
                placeholderTextColor={"gray"}
                value={searchQuery}
                onChangeText={setSearchQuery}
              />
              </View>
          </View>

            
            <FlatList
              data={filteredTrails}
              renderItem={renderTrail}
              keyExtractor={(item) => item.id.toString()}
              showsVerticalScrollIndicator={false}
              style={styles.list}
              contentContainerStyle={styles.listContent}
          />
      </SafeAreaView>
    </KeyboardAvoidingView>
  );
};

const styles = StyleSheet.create({
topBar: {
  backgroundColor: cores.uminho, 
  alignItems: 'center',
  paddingHorizontal: 12,
  height: 70, 
  width: '100%',
  marginBottom: 20
},
inputContainer: {
  marginTop: 15,
  flexDirection: 'row',
  alignItems: 'center',
  width: '95%',
  height: 40,
  borderColor: 'gray',
  borderWidth: 1,
  borderRadius: 25,
  paddingHorizontal: 10,
  backgroundColor: 'white'
},
searchIcon: {
    marginRight: 10,
},
input: {
  flex: 1,
  paddingVertical: 0,
  color: 'black'
},
  container: {
    flex: 1,
    backgroundColor : 'white',
    //marginBottom: 50
  },
  list: {
    flex: 1,
    width: '100%',
},
listContent: {
    alignItems: 'center',
    paddingBottom:60
},
  trailItem: {
      height: 150,
      width: 300,
      flexDirection: 'row',
      marginBottom: 0,
      alignItems: 'center',
      //borderColor: 'gray',
      borderWidth: 0.5,
      borderTopColor: 'gray',
      borderBottomColor: 'gray',
      borderRightColor: 'white',
      borderLeftColor: 'white',
    },
    trailImage: {
      width: 100,
      height: 100,
      borderRadius: 8,
    },
    trailName: {
      paddingHorizontal: 15,
      marginTop: 8,
      fontSize: 16,
      color : 'black',
      fontWeight: 'bold',
    }
});

export default Search