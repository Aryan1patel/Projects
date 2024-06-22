package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit

import androidx.compose.material3.AlertDialog

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

data class Shoppingitems(var id:Int,
                         var name:String,
                         var quantity:Int,
                         var isEditing :Boolean=false){
}

//   // to make sure that the is not lost while roating the screen we make a class of viewmodle and use it


@Composable
fun ShoppinglistApp()  {

    // here we have to make a new var variable for viewmodle for sitem to make sure the data is not erased and also have to edit in below


    var sitem by remember { mutableStateOf(listOf<Shoppingitems>()) }    // list of item that contain in the shoppingitem data class
    var showDialog by remember { mutableStateOf(false) }
    var itemname by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {

        val context = LocalContext.current

        Button(
            onClick = { showDialog=true},
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Add item")
        }

        LazyColumn(                     // impliment scrolling or grid/list
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)

        ) {  // it reserve all height of the screen if not set

            items(sitem) {// sitem is a list or a datasourse for items
               // ShoppinglistItems(it,{},{})  // a new type of item that we make in function

                item->                 // it means it itrates in the list (sitem) and find the item and make changes to it
                if(item.isEditing){                  // it means that it traverse in the sitem list and check that if isedit is true and then run the shopping itemedit funtion and other which are false show them on list or shoppinglistitem funtion
                    ShoppingItemedit(      // //            // it calls the edit function that display edit funtionality to the display
                        item = item,  // getting the current `Shoppingitems` instance because of here  item :Shoppingitems,     //  // Passing the current `Shoppingitems` instance   // and this passes all the value of id name and quantity of orinial list item after finding in the itration
                        onEditComplete = { editedname,editedquantity ->    // its gets the value of newname and newquantity because we pass the parameter in the main funtion of shoppingitemedit                // these are new typed one for editing

                        sitem=sitem.map { it.copy(isEditing = false)} // // its not need to make all false because all are already false and when we click on save then the current edited on become false and shown in list // when editing done make all list item isediting false so they can be shown in the list
                        val editedItem = sitem.find { it.id== item.id }   // this edited item store all the value of the list box after searching from which we select edit icon
                        editedItem?.let {    // edititem could be empty so use let

                            it.name=editedname             // take the editname from the shoppingitemedit funtion and chage the name of the specific list name
                            it.quantity=editedquantity     // it mean the 'item' which hold the current value of list
                        }


                    },
                        onCancle = {
                            sitem = sitem.map { it.copy(isEditing = false) } // Reset all to false
                        }

                    )

                }
                else{                      //  // also mean when we click on add it check if edit is click or not // and also when we click on save


                   ShoppinglistItems(item = item,    // running the shoppinglistitem show them on display

                       onEditclick = {      // this is the onedit function it run when we click on edit button
                       // finding which item we are editing and setting the isediting to true so that it can show shoppingitemedit above
                       sitem=sitem.map { it.copy(isEditing = it.id==item.id) }},  //this is finding the id in the sitem and then making that one list item isediting true
                                     // now isediting is true the that if (isediting) runs
                                     // and
                       onDeleteclick = {
                           sitem=sitem-item }  // remove this selected item from the list sitem
                   )
                }
            }


        }

    }

    if(showDialog){

        AlertDialog(onDismissRequest = { showDialog=false},
                    confirmButton = { // here we have to give the button which are gonna be on the popup window . confirmbutton is a parameter in the alertdiog

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Button(onClick = {
                                if(itemname.isNotBlank()){

                                    val newitem = Shoppingitems(        // shoppingitems is a class we make it take following things
                                        id=sitem.size+1,                  // Creating a New Instance: When you create newitem, you're creating a new instance of Shoppingitems with specified values:
                                        name=itemname,
                                        quantity = itemQuantity.toIntOrNull()?:1 // if not intornull and elvis operater ?:1 then the app crash when we save without quantity input now it will store 1 if we do not give the quantity input  // taking the input from the second outlined box as string and converting into int
                                    )
                                    sitem=sitem+newitem  // onclick  it make a newitem or new instance and add in the sitem list with all the new name and quantity because of newitem instance above
                                    showDialog=false     // and on clicking add it close pop up
                                    itemname=""          // and inputname again to empty
                                    itemQuantity =""

                                }
                            }) {

                                Text(text = "Add")

                            }

                            // space between make space between the button as much as it can

                            Button(onClick = { showDialog = false }) {

                                Text(text = "Cancle")

                            }
                        }
                    },
                    title={Text("Add shopping items")},  // it set the title of the popup box

                    text={

                        Column {

                            // put the confirmbutton code in this to add the button in the top of the alert box

                           // Spacer(modifier = Modifier.height(8.dp))

                            Column {

                                OutlinedTextField(
                                    value = itemname,                     // it stores the input value in the itemname var
                                    onValueChange = { itemname = it },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    label = { Text(text = "item name") }
                                )

                                Spacer(modifier = Modifier.height(8.dp))


                                OutlinedTextField(
                                    value = itemQuantity,
                                    onValueChange = { itemQuantity = it },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    label = { Text(text = "item quantity")}
                                )
                            }
                        }
                    }
        )
    }  // pop up window to take user input when we click in add button
}







@Composable
fun ShoppinglistItems(           // this function is going to show in list is lazycolumn (when itration goes in else part)     // also contain edit and delete option
    item :Shoppingitems,     //  // Passing the current `Shoppingitems` instance  // here item store all the value of shoppingitems of diffrent list diffrent value
    onEditclick: () -> Unit,   // same thing happen in onclick function // it mean it doesnt return anything but execute the code in it //
    onDeleteclick: () -> Unit
){

    Row (
        modifier= Modifier
            .padding(6.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color.DarkGray),
                shape = RoundedCornerShape(15)
            ),
        horizontalArrangement = Arrangement.SpaceBetween  ,      // it give the maximum space in horizontal which is possible between anything in this row

        verticalAlignment = Alignment.CenterVertically         // because the item name showing in top and the item icon showing in bottom so make all bottom

    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Oty: "+item.quantity.toString(), modifier = Modifier.padding(8.dp))

        Row (modifier=Modifier.padding(6.dp)){

            IconButton(onClick = { onEditclick()}) {   // it goes in lazycoloum oneditclick of else section(because shopping list item in else section) function and make isediting true and in lazycoloum isediting function call

                Icon(imageVector =  Icons.Default.Edit,contentDescription = null )   // it give us the icon of edit button
                // we can change the size of icon by adding modifier in the icon
            }

            IconButton(onClick = { onDeleteclick() }) {

                Icon(imageVector = Icons.Default.Delete,contentDescription = null )

            }

        }





    }


}


@Composable
fun ShoppingItemedit(       // we can also use alertdialog in it    // when item itrate after if section and find ifediting in lazy coloum

    item:Shoppingitems,
    onEditComplete: (String ,Int) -> Unit  ,  // ittakes the the newname and newquantity which we pass in the last when we click in the save button // in this we are taking string and int as item name and item quantity to change those
    onCancle: () -> Unit

){

    var editname by remember { mutableStateOf(item.name) }
    var editquantity by remember { mutableStateOf(item.quantity.toString()) }

    var isEditing by remember { mutableStateOf(item.isEditing) }

    Column (horizontalAlignment = Alignment.CenterHorizontally) {


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Column {
                OutlinedTextField(
                    value = editname, onValueChange = { editname = it },     // diffrent than editedname
                    singleLine = true, modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                ) // wrap context size take the space that it only needs


                OutlinedTextField(
                    value = editquantity, onValueChange = { editquantity = it },
                    singleLine = true, modifier = Modifier
                        .wrapContentSize()
                        .padding(8.dp)
                ) // wrap context size take the space that it only needs

            }


        }
        
        Row {


            Button(onClick = {
                isEditing = false
                onEditComplete(           // this funtion also make isediting false for all list in item so that they can be shown in list
                    editname,             // this passing the new value in parameter name oneditComplete of edit function
                    editquantity.toIntOrNull() ?: 1
                )   // passing the value of name and quantity as a parameter
                // we have to convert the editquantity to integer because in starting oneditcomplete we give lamda a string and a integer
            }) {

                Text(text = "Save")
            }
            
            Spacer(modifier = Modifier.padding(8.dp))
            
            Button(onClick = {
                onCancle()}) {
                Text(text = "Cancle")
            }

        }

    }

}


// frist shoppinglistapp run
// the it show add item button if click then showdialog function call and then outertextfiled takes user input and store in item name and item quantity
// and when add button call then it store the itemname ,itemquantity and id in the list by new instance
// and a function name lazycolum contain list which scroll and it contain predefined funtion items in which we pass slist it contain all value
// in this it itrate in all list it check it is on edit mode or not if true then show edit type list and when not then show values of list
// in that when item.isedit is true it call shoppingitemedit (edit funtion) and when
// the item.isedit is false then goes on else part and call shoppinglistitem (it display the item in list when itrating)


