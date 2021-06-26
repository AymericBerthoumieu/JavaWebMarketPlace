package no.oslomet.clientrestproject.controller;

import no.oslomet.clientrestproject.model.Order;
import no.oslomet.clientrestproject.model.Product;
import no.oslomet.clientrestproject.model.Shipping;
import no.oslomet.clientrestproject.model.User;
import no.oslomet.clientrestproject.service.OrderService;
import no.oslomet.clientrestproject.service.ProductService;
import no.oslomet.clientrestproject.service.ShippingService;
import no.oslomet.clientrestproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private ProductService productService;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    public static String uploadDirectory = System.getProperty("user.dir")+"/uploads"; //src/main/resources/static/images   /uploads
    public long indice = 0;

//=============================== HOME ===============================
    @GetMapping("/")
    public String home(Model model){
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);

        return "index";
    }

    @GetMapping("/home")
    public String homePage(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        if (user.getRoles().equals("ADMIN")) {
            List<User> allUsers = userService.getAllUsers();
            List<User> users = new ArrayList<>();
            for (User u: allUsers) {
                if (!u.getRoles().equals("ADMIN")) {
                    users.add(u);
                }
            }
            model.addAttribute("users", users);
            return "indexAdmin";
        }

        if (user.getRoles().equals("MERCHANT")) {
            List<Long> productsIds = user.getProducts();
            List<Product> products = new ArrayList<>();
            for (Long id: productsIds) {
                products.add(productService.getProductById(id));
            }
            model.addAttribute("products", products);
            return "indexMerchant";
        }

        List<Product> products = productService.getAllProducts();
        List<Long> likes = user.getProducts();
        model.addAttribute("products", products);
        model.addAttribute("likes", likes);
        return "index";
    }

    @PostMapping("/searchProduct")
    public String searchProduct(@RequestParam String search, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        List<Product> products = productService.getAllProducts();
        List<Product> searchedProducts = new ArrayList<>();

        for (Product p : products) {
            String stringP = p.toStringSearch();
            int isInclude = stringP.indexOf(search);
            if (isInclude >= 0) {
                searchedProducts.add(p);
            }
        }

        List<Long> likes =new ArrayList<>();
        if (user!=null) {
            likes = user.getProducts();
        }


        model.addAttribute("user", user);
        model.addAttribute("likes", likes);
        model.addAttribute("products", searchedProducts);
        return "index";
    }

    @GetMapping("/DetailProductOut/{id}")
    public String detailProductOut(Model model, @PathVariable Long id) {

        Product product = productService.getProductById(id);
        model.addAttribute("prod", product);

        List<Long> stars = product.getStars();
        long note =0;
        if (!stars.isEmpty()) {
            long nb =0;
            for (Long i : stars) {
                note = note + i;
                nb = nb + 1;
            }
            note = note / nb;
        }
        model.addAttribute("note", note);

        return "detailProductOut";
    }


//=============================== LOGIN ===============================

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @PostMapping("/processRegistration")
    public String register(@ModelAttribute("user") User user){
        String noopPassword = user.getPassword();
        String email = user.getEmail();
        List<User> users = userService.getAllUsers();
        for (User u : users) {
            if (email.equals(u.getEmail())){
                return ("redirect:/EmailUsed");
            }
        }
        String hashPassword = bCryptPasswordEncoder.encode(noopPassword);
        user.setPassword(hashPassword);
        userService.saveUser(user);
        return "redirect:/login";
    }

//=============================== USER PAGE ===============================
    @GetMapping("/UserPage")
    public String UserPage(Model model ){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("ships", user.getShippings());

        return "UserPage";
    }

    @GetMapping("/ChangeNames")
    public String changeNames(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "formNames";
    }

    @PostMapping("/processNames")
    public String updateNames(@ModelAttribute("user") User newUser,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());

        userService.updateNameUser(user.getEmail(), user);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/ChangeEmail")
    public String changeEmail(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        return "formEmail";
    }

    @PostMapping("/processEmail")
    public String updateEmail(@ModelAttribute("user") User newUser,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        String oldEmail = user.getEmail();
        String newEmail = newUser.getEmail();
        if (newEmail.equals(oldEmail)) {
            model.addAttribute("user", user);
            return "redirect:/home";}

        List<User> users = userService.getAllUsers();
        for (User u : users) {
            if (newEmail.equals(u.getEmail())){
                return ("redirect:/EmailUsed");
            }
        }

        user.setEmail(newEmail);

        userService.updateEmailUser(oldEmail, user);
        model.addAttribute("user", user);
        return "redirect:/login";
    }

    @GetMapping("/ChangePW")
    public String changePW(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);
        model.addAttribute("wrongMatch", false);
        return "formPW";
    }

    @PostMapping("/processPW")
    public String updatePW(@RequestParam("PW1") String PW1, @RequestParam("PW2") String PW2,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        Boolean wrongMatch = false;


        if (!PW1.equals(PW2)){
            wrongMatch = true;
            model.addAttribute("user", user);
            model.addAttribute("wrongMatch", wrongMatch);
            return "formPW";
        }


        String hashPassword = bCryptPasswordEncoder.encode(PW1);

        if (hashPassword.equals(user.getPassword())){
            model.addAttribute("user", user);
            return "redirect:/home";
        }

        user.setPassword(hashPassword);

        userService.updatePWUser(user.getEmail(), user);

        model.addAttribute("wrongMatch", wrongMatch);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/NewShipping")
    public String createShipping(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        Shipping ship = new Shipping();
        model.addAttribute("ship", ship);

        return "formShipping";
    }

    @PostMapping("/saveShipping")
    public String saveShip(@ModelAttribute("ship") Shipping ship, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        shippingService.saveShipping(ship, user.getUser_id());
        return "redirect:/UserPage";
    }

//=============================== ADMIN RIGHTS ===============================

    @GetMapping("/DeleteUser/{email}")
    public String deleteUser(Model model, @PathVariable String email){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        String userEmail = user.getEmail();

        if ((!user.getRoles().equals("ADMIN"))
                && (!userEmail.equals(email))){
            return "redirect:/sorry";
        }

        if ((user.getRoles().equals("ADMIN"))
                && (userEmail.equals(email))){
            return "redirect:/sorry2";
        }

        User toDelete = userService.getUserByEmail(email);
        if (toDelete.getRoles().equals("MERCHANT")){
            List<Long> products = toDelete.getProducts();
            for (Long p : products) {
                productService.deleteProduct(p);
            }
        }

        if (toDelete.getRoles().equals("USER")) {
            List<Order> orderToDelete = toDelete.getOrders();
            if (orderToDelete != null) {
                for (Order o : orderToDelete) {
                    orderService.deleteOrder(o.getOrder_id());
                }
            }

            List<Shipping> shippingToDelete = toDelete.getShippings();
            if (shippingToDelete != null) {
                for (Shipping s : shippingToDelete) {
                    shippingService.deleteShipping(s.getShipping_id());
                }
            }
        }


        userService.deleteUser(toDelete.getUser_id());
        model.addAttribute("user", user);

        // if the user deletes it's own account, it he must be redirected to /
        if (userEmail.equals(email)){
            return "redirect:/";
        }

        return "redirect:/home";
    }

    @GetMapping("/ChangePW/{email}")
    public String changePW(Model model, @PathVariable String email){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        if (!user.getRoles().equals("ADMIN")){
            return "redirect:/sorry";
        }

        User toChange = userService.getUserByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("toChange", toChange);
        return "formPWadmin";
    }

    @PostMapping("/processPWadmin")
    public String updatePWadmin(@ModelAttribute("toChange") User newUser,Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        String noopPassword = newUser.getPassword();
        String hashPassword = bCryptPasswordEncoder.encode(noopPassword);

        User toChange = userService.getUserByEmail(newUser.getEmail());
        toChange.setPassword(hashPassword);

        userService.updatePWUser(newUser.getEmail(), toChange);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/DetailUser/{email}")
    public String detailUser(Model model, @PathVariable String email){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        if (!user.getRoles().equals("ADMIN")){
            return "redirect:/sorry";
        }

        User toDetail = userService.getUserByEmail(email);

        model.addAttribute("user", user);
        model.addAttribute("toDetail", toDetail);
        return "detailUser";
    }





//=============================== USER INTERACTIONS WITH PRODUCTS ===============================

    @GetMapping("/LikeProduct/{id}")
    public String likeProduct(@PathVariable long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        List<Long> likedProducts = user.getProducts();
        likedProducts.add(id);
        user.setProducts(likedProducts);
        userService.updateUser(user.getEmail(), user);

        Product product = productService.getProductById(id);
        long likes = product.getLikes();
        product.setLikes(likes+1);

        productService.updateProduct(id, product);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/UnLikeProduct/{id}")
    public String unlikeProduct(@PathVariable long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        List<Long> likedProducts = user.getProducts();
        likedProducts.remove(id);
        user.setProducts(likedProducts);
        userService.updateUser(user.getEmail(), user);

        Product product = productService.getProductById(id);
        long likes = product.getLikes();
        product.setLikes(likes-1);

        productService.updateProduct(id, product);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/likes")
    public String likeProduct(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        List<Long> idProducts = user.getProducts();
        List<Product> likedProducts = new ArrayList<>();
        List<Product> products = productService.getAllProducts();

        //Only way to deal with a suppression of product while it is in likes
        for (Product p: products) {
            if (idProducts.contains(p.getId())){
                likedProducts.add(p);
            }
        }

        model.addAttribute("products", likedProducts);
        model.addAttribute("user", user);
        return "likedProducts";
    }

    @GetMapping("/DetailProduct/{id}")
    public String detailProduct(Model model, @PathVariable Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        Product product = productService.getProductById(id);
        model.addAttribute("prod", product);

        Boolean liked = false;
        if (user.getProducts().contains(id)){
            liked =true;
        }
        model.addAttribute("liked", liked);

        Boolean starred = false;
        if (user.getStarred().contains(id)){
            starred =true;
        }
        model.addAttribute("starred", starred);

        List<Long> stars = product.getStars();
        long note =0;
        if (!stars.isEmpty()) {
            long nb =0;
            for (Long i : stars) {
                note = note + i;
                nb = nb + 1;
            }
            note = note / nb;
        }
        model.addAttribute("note", note);

        List<String> quantities =new ArrayList<>();
        for (int i=1; i<=product.getQuantity(); i++) {
            quantities.add(""+i);
        }
        model.addAttribute("quant", quantities);

        return "detailProduct";
    }

    @PostMapping("/star")
    public String star(Model model, @RequestParam("note") String note, @RequestParam("id") Long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        // add to starred product
        List<Long> starred = user.getStarred();
        starred.add(id);
        user.setStarred(starred);
        userService.updateUser(user.getEmail(), user);

        // add the note to the product
        Long newNote= new Long(0);
        if (note.equals("*")){ newNote =new Long(1);}
        if (note.equals("**")){ newNote =new Long(2);}
        if (note.equals("***")){ newNote =new Long(3);}
        if (note.equals("****")){ newNote =new Long(4);}
        if (note.equals("*****")){ newNote =new Long(5);}
        Product prod = productService.getProductById(id);
        List<Long> notes = prod.getStars();
        notes.add(newNote);
        prod.setStars(notes);
        productService.updateProduct(id, prod);

        String redirection = "redirect:/DetailProduct/"+id;
        return (redirection);
    }

    @PostMapping("/Order")
    public String order(Model model, @RequestParam("id") Long id, @RequestParam Long quantity){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        Long userId = user.getUser_id();

        Product prod = productService.getProductById(id);
        if (quantity>prod.getQuantity()){
            return"redirect:/sorry3";
        }

        for (int i =1; i<=quantity; i++){
            orderService.orderProduct(userId, id);
        }

        prod.setQuantity(prod.getQuantity()-quantity);
        productService.updateProduct(prod.getId(),prod);
        return "redirect:/home";
    }

    @GetMapping("/listOrder")
    public String listOrder(Model model ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        List<Order> ordersList = user.getOrders();
        List<Shipping> shippingList = user.getShippings();

        Order currentOrder = null;
        for (Order order : ordersList) {
            if (order.getShipping() == null) {
                currentOrder = order;
            }
        }

        if (currentOrder != null){
            List<Long> ids = currentOrder.getProducts();
            List<Product> products = new ArrayList<>();
            //avoid error if a merchant delete a product which is in an order
            List<Product> productsList = productService.getAllProducts();
            for (Product prod : productsList) {
                if (ids.contains(prod.getId())) {
                    Long nb =new Long(0);
                    for (Long id : ids) {
                        if (id==prod.getId()){nb=nb+1;}
                    }
                    prod.setQuantity(nb);
                    products.add(prod);
                }
            }
            model.addAttribute("products", products);
        }


        ordersList.remove(currentOrder);
        model.addAttribute("orders", ordersList);
        model.addAttribute("currentOrder", currentOrder);
        model.addAttribute("shippingList", shippingList);
        return "Orders";
    }

    @PostMapping("/shipOrder/{id}")
    public String shipOrder(@RequestParam("shipping") long shippingId, @PathVariable("id") Long id, Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);


        Order currentOrder = orderService.getOrderById(id);

        Shipping shipping = shippingService.getShippingById(shippingId);
        currentOrder.setShipping(shipping);
        orderService.updateOrder(currentOrder, user.getUser_id());
        return "redirect:/listOrder";
    }

    @GetMapping("/detailOrder/{id}")
    public String detailOrder(@PathVariable("id") Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        Order currentOrder = orderService.getOrderById(id);


        List<Long> ids = currentOrder.getProducts();
        List<Product> products = new ArrayList<>();
        //avoid error if a merchant delete a product which is in an order
        List<Product> productsList = productService.getAllProducts();
        for (Product prod : productsList) {
            if (ids.contains(prod.getId())) {
                Long nb =new Long(0);
                for (Long i : ids) {
                    if (i==prod.getId()){nb=nb+1;}
                }
                prod.setQuantity(nb);
                products.add(prod);
            }
        }

        for (Long i : ids){
            Boolean bool = false;
            for (Product p: products) {
                if (i==p.getId()){
                    bool=true;
                }
            }

            if (!bool){
                Product prod = new Product();
                prod.setName(""+i);
                prod.setQuantity(1);
                products.add(prod);
            }

        }
        model.addAttribute("products", products);

        Shipping ship = currentOrder.getShipping();

        model.addAttribute("order", currentOrder);
        model.addAttribute("products", products);
        model.addAttribute("ship", ship);

        return "detailShipping";
    }


//=============================== MERCHANT INTERACTIONS WITH PRODUCTS ===============================

    @GetMapping("/CreateProduct")
    public String createProduct(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        if (!user.getRoles().equals("MERCHANT")){
            return "redirect:/sorry";
        }

        Product product = new Product();

        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "formProduct";
    }

    @PostMapping("/saveProduct")
    public String saveProduct(@ModelAttribute("toChange") Product newProduct, Model model, @RequestParam("files") MultipartFile file){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        StringBuilder fileName = new StringBuilder();
        Path fileNameAndPath = Paths.get(uploadDirectory, indice+file.getOriginalFilename());
        fileName.append(file.getOriginalFilename());

        try {
            Files.write(fileNameAndPath, file.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (newProduct.getId()==0) {
            long merchantId = user.getUser_id();

            newProduct.setPicture(""+indice+fileName); //fileNameAndPath

            newProduct.setMerchant(merchantId);
            productService.saveProduct(newProduct);

            List<Product> products = productService.getAllProducts();
            List<Long> ids = new ArrayList<>();

            for (Product p : products) {
                if (p.getMerchant() == merchantId) {
                    ids.add(p.getId());
                }
            }

            user.setProducts(ids);

            userService.updateUser(user.getEmail(), user);
        }
        else {
            Product product = productService.getProductById(newProduct.getId());
            product.setName(newProduct.getName());
            product.setQuality(newProduct.getQuality());
            product.setDescription(newProduct.getDescription());
            product.setQuantity(newProduct.getQuantity());
            StringBuilder newString = new StringBuilder();
            if (!fileName.toString().equals(newString.toString())) {
                product.setPicture("" + indice + fileName); // fileNameAndPath
            }
            productService.updateProduct(product.getId(), product);
        }
        model.addAttribute("user", user);
        indice=indice+1;
        return "redirect:/home";
    }

    @GetMapping("/DeleteProduct/{id}")
    public String deleteProduct(Model model,@PathVariable long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        if (!user.getRoles().equals("MERCHANT")){
            return "redirect:/sorry";
        }

        productService.deleteProduct(id);
        List<Long> ids = user.getProducts();
        ids.remove(id);
        user.setProducts(ids);
        userService.updateUser(user.getEmail(),user);
        model.addAttribute("user", user);
        return "redirect:/home";
    }

    @GetMapping("/UpdateProduct/{id}")
    public String updateProduct(Model model,@PathVariable long id){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());

        if (!user.getRoles().equals("MERCHANT")){
            return "redirect:/sorry";
        }
        Product product = productService.getProductById(id);

        model.addAttribute("user", user);
        model.addAttribute("product", product);
        return "formProduct";
    }

//=============================== Errors ===============================

    @GetMapping("/EmailUsed")
    public String emailUsed(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        return "EmailUsed";
    }

    @GetMapping("/sorry")
    public String noRight(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        return "sorry";
    }

    @GetMapping("/sorry2")
    public String noDeleteAdmin(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        return "sorry2";
    }

    @GetMapping("/sorry3")
    public String problemQuant(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(auth.getName());
        model.addAttribute("user", user);

        return "sorry3";
    }

}
