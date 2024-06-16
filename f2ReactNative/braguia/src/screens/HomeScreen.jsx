import { View, Text, Image  } from 'react-native';
import { StyleSheet, FlatList, TouchableOpacity, Alert} from 'react-native';
import React, { useEffect, useState, useContext } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigation } from '@react-navigation/native';
import { updateUsername } from '../actions/user';
import { updateAppInfo, setTrails } from '../actions/appData';
import {cores, api} from '../var.js'
import Icon from 'react-native-vector-icons/MaterialIcons';
/* TO-DO:
- Meter isto a funcionar com redux
- Melhorar design / por a funcionar dark/ligth thene
- Colocar clicaveis os vários trails e redirecionar
-
Extra
- Carregar à medida que vamos fazendo scroll !

*/

const HomeScreen = () => {

    const [title, setTitle] = useState("Title");
    const [appInfo, setAppInfo] = useState("Loading");
    const [appDesc, setAppDesc] = useState("Loading");
    //const [trails, setTrails] = useState([]);

    const trails  = useSelector(state => state.data.appData.trails);
    const appinfo = useSelector(state => state.data.appData.appinfo);

    const navigation = useNavigation();

    const userType= useSelector((state) => state.data.user.user_type);

  
    const updateInfo = async () => {

        setTitle(appinfo.app_name);
        setAppInfo(appinfo.app_desc);
        setAppDesc(appinfo.app_landing_page_text);
        //console.log(data[0].app_landing_page_text);

    };
    

    useEffect(() => {
      updateInfo();
    }, []);
    // <Image source={{ uri: item.image }} style={styles.trailImage} />

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
          <View style={styles.trailDescContainer}>
            <Icon size={15} color="black" name="access-time" style={styles.searchIcon}/>
            <Text style={styles.trailDuration}>{item.trail_duration}</Text>
          </View>
        </TouchableOpacity>
      );


  
    return (
      <View style={styles.container}>
        <View style={styles.topBar}>
          <Image source={require('../../assets/logo_white.png')} style={styles.logo} />
          <Text style={styles.title}>{appInfo}</Text>
        </View>

        <View style={styles.container2}>
          <Text style={[styles.outTrails, { textAlign: 'left', alignSelf: 'flex-start', marginLeft: 0}]}>{"Our Trails"}</Text>

          
          <FlatList
          data={trails}
          renderItem={({ item }) => renderTrail({ item, userType: userType })}
          keyExtractor={(item) => item.id.toString()}
          horizontal
          showsHorizontalScrollIndicator={false}
        />
          <Text style={styles.appDesc}>{appDesc}</Text>
          </View>
      </View>
    );
};

const styles = StyleSheet.create({
    container: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      padding: 0,
      backgroundColor : 'white'
  
    },
    container2: {
      flex: 1,
      justifyContent: 'center',
      alignItems: 'center',
      padding: 0,
      backgroundColor : 'white',
      paddingHorizontal: 12
    },
    topBar: {
      flexDirection: 'row',
      backgroundColor: cores.uminho, 
      alignItems: 'center',
      paddingHorizontal: 12,
      height: 70, 
      width: '100%',
      marginBottom: 20
    },
    title: {
      paddingVertical: 15,
      paddingHorizontal: 10,
      fontSize: 18,
      fontWeight: 'bold',
      color : 'white'
    },
    logo: {
      paddingVertical: 15,
      width: 30,
      height: 40,
      borderRadius: 8
    },
    outTrails: {
      fontSize: 18,
      fontWeight: 'bold',
      marginBottom: 15,
      color : 'black',
      textAlign: 'left'
    },
    appDesc: {
      fontSize: 16,
      fontWeight: 'normal',
      marginBottom: 290,
      color : 'black'
    },
    trailItem: {
        marginRight: 16,
        alignItems: 'center',
        height: 200,
        backgroundColor: '#FFF7F7',
        borderRadius: 15,
        elevation: 4
      },
      trailImage: {
        width: 150,
        height: 150,
        borderTopLeftRadius: 15,
        borderTopRightRadius: 15
      },
      trailName: {
        marginTop: 8,
        fontSize: 14,
        color : 'black',
        fontWeight: '600'
      },
      trailDescContainer: {
        marginTop: 2,
        flexDirection: 'row',
        justifyContent: 'left',
        alignItems: 'left',
      },
      trailDuration: {
        paddingHorizontal: 3,
        fontSize: 12,
        color : 'black',
        fontWeight: '3~400'
      }
  });
export default HomeScreen;