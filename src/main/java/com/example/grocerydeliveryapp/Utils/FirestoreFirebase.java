package com.example.grocerydeliveryapp.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.grocerydeliveryapp.interfaces.AddressCallBack;
import com.example.grocerydeliveryapp.interfaces.CartCallBack;
import com.example.grocerydeliveryapp.interfaces.DataAddedCallBack;
import com.example.grocerydeliveryapp.interfaces.OrderCallBack;
import com.example.grocerydeliveryapp.interfaces.ProductCallBack;
import com.example.grocerydeliveryapp.model.Address;
import com.example.grocerydeliveryapp.model.Cart;
import com.example.grocerydeliveryapp.model.Order;
import com.example.grocerydeliveryapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirestoreFirebase {

    private final String TAG = this.getClass().getName();
    private FirebaseFirestore db;
    private Context mContext;

    public FirestoreFirebase(Context context) {
        mContext = context;
        db = FirebaseFirestore.getInstance();
    }

    public void getProductFromFirebase(ProductCallBack callBack) {
        final ArrayList<Product> products = new ArrayList<>();
        db.collection("Products").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Product product = document.toObject(Product.class);
                                products.add(product);
                            }
                            callBack.onComplete(products);
                        }
                    }
                });
    }

    public void putUserInfoOnFirestore(FirebaseUser user) {
        // Create a new user with a first and last name
        Map<String, Object> userData = new HashMap<>();
        userData.put("Number", user.getPhoneNumber());

        // Add a new document with a generated ID
        db.collection("users").document(user.getPhoneNumber())
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void addProductToCart(String docId, Product product, String quantity, int shopID, final DataAddedCallBack callBack) {
        Cart cart;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef;
        if (docId != null) {
            collRef = db.collection("users").
                    document(user.getEmail()).
                    collection("Cart").
                    document(docId);
            cart = new Cart(docId, product, Integer.parseInt(quantity), shopID);
        } else {
            collRef = db.collection("users").
                    document(user.getEmail()).
                    collection("Cart").
                    document();
            cart = new Cart(null, product, Integer.parseInt(quantity), shopID);
        }

        collRef.set(cart).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onSuccess(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                callBack.onFailure(true);
            }
        });
    }

    public void getProductsFromCart(final CartCallBack callBack) {
        final ArrayList<Cart> cartList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users/" + user.getEmail() + "/Cart").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cart cart = document.toObject(Cart.class);
                                cart.setCartId(document.getId());
                                cartList.add(cart);
                            }
                            callBack.onComplete(cartList);
                        }
                    }
                });
    }

    public void removeProductFromCart(String docId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef = db.collection("users").
                document(user.getEmail()).
                collection("Cart").
                document(docId);
        collRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(mContext, "Product removed from cart", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addAddressIntoFirebase(String docId, Address address, final DataAddedCallBack callBack) {
        Map<String, String> addressData = new HashMap<>();
        addressData.put("Name", address.getName());
        addressData.put("Address", address.getAddress());
        addressData.put("Pincode", address.getPincode());
        addressData.put("City", address.getCity());
        addressData.put("State", address.getState());
        addressData.put("Country", address.getCountry());
        addressData.put("Mobile Number", address.getMobileNo());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference collRef;
        if (docId != null) {
            collRef = db.collection("users").
                    document(user.getEmail()).
                    collection("Addresses").
                    document(docId);
        } else {
            collRef = db.collection("users").
                    document(user.getEmail()).
                    collection("Addresses").
                    document();
        }

        collRef.set(addressData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onSuccess(true);
                Toast.makeText(mContext, "Address saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAddressFromFirebase(final AddressCallBack callBack) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users/" + user.getEmail() + "/Addresses").
                get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Address> addresses = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String addressId = document.getId();
                                String name = (String) document.get("Name");
                                String add = (String) document.get("Address");
                                String pincode = (String) document.get("Pincode");
                                String city = (String) document.get("City");
                                String state = (String) document.get("State");
                                String country = (String) document.get("Country");
                                String mobileNo = (String) document.get("Mobile Number");

                                Address address = new Address(addressId, name, add, pincode, city, state, country, mobileNo);
                                addresses.add(address);
                            }

                            callBack.onComplete(addresses);
                        }
                    }
                });
    }

    public void placeOrder(final OrderCallBack callBack) {
        getProductsFromCart(new CartCallBack() {
            @Override
            public void onComplete(ArrayList<Cart> cartList) {
                int totalPrice = 0;
                for (Cart cart : cartList) {
                    totalPrice += (cart.getQuantity() * cart.getProduct().getPrice());
                }
                Order order = new Order(null, cartList, null, totalPrice);
                final int totalAmount = totalPrice;
                db.collection("Orders").
                        document("0").
                        get().
                        addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                int orderID = 0;
                                if (task.isSuccessful()) {
                                    DocumentSnapshot result = task.getResult();
                                    String lastOrderID = (String) result.get("LastOrderID");

                                    if (lastOrderID == null || Integer.parseInt(lastOrderID) == 0) {
                                        orderID = 10001001;
                                    } else {
                                        orderID = Integer.parseInt(lastOrderID) + 1;
                                    }
                                    if (orderID > 0) {
                                        final String finalOrderId = String.valueOf(orderID);
                                        order.setOrderID(finalOrderId);
                                        db.collection("Orders").
                                                document("GDA" + finalOrderId).
                                                set(order).
                                                addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        db.collection("users/" + user.getEmail() + "/Orders").
                                                                document("GDA" + finalOrderId).
                                                                set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        putLastOrderIdToFirestore(finalOrderId, callBack);
                                                                    }
                                                                });
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(mContext, "Something went Wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                }
                            }
                        });
            }
        });
    }

    private void putLastOrderIdToFirestore(final String finalOrderId, final OrderCallBack callBack) {
        Map<String, Object> data = new HashMap<>();
        data.put("LastOrderID", finalOrderId);
        db.collection("Orders").document("0").set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                callBack.onComplete("BM" + finalOrderId);
                Toast.makeText(mContext, "Order Placed Succesfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void emptyCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db.collection("users/" + user.getEmail() + "/Cart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    db.collection("users").
                            document(user.getEmail()).
                            collection("Cart").
                            document(document.getId()).delete();
                }
            }
        });

    }

}
