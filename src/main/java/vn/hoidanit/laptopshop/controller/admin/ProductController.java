package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.model.Product;
import vn.hoidanit.laptopshop.model.User;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {
    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getProductPage(Model model) {
        List<Product> products = this.productService.getAllProducts();
        model.addAttribute("products", products);
        return "/admin/product/show";
    }

    @RequestMapping("/admin/product/create") // GET
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @RequestMapping(value = "/admin/product/create", method = RequestMethod.POST)
    public String createproduct(Model model, @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult bindingResult,
            @RequestParam("fileUpload") MultipartFile file) {
        List<FieldError> errors = bindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>" + error.getField() + " - " + error.getDefaultMessage());
        }

        if (bindingResult.hasErrors()) {
            return "/admin/product/create";
        }

        String avatar = this.uploadService.handleSaveUploadFile(file, "product");
        newProduct.setImage(avatar);

        this.productService.handleSaveProduct(newProduct);
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/{id}")
    public String getProductDetailPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }

    @RequestMapping("/admin/product/delete/{id}") // GET
    public String DeleteAProductPage(Model model, @PathVariable long id) {
        Product productDelete = new Product();
        productDelete.setId(id);
        model.addAttribute("id", id);
        model.addAttribute("product", productDelete);
        return "admin/product/delete";
    }

    @RequestMapping("/admin/product/delete")
    public String DeleteAProduct(Model model, @ModelAttribute("product") User product) {

        this.productService.deleteAProduct(product.getId());
        return "redirect:/admin/product";
    }

    @RequestMapping("/admin/product/update/{id}") // GET
    public String getUpdateUserPage(Model model, @PathVariable long id) {
        Product product = this.productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/update";
    }

    @RequestMapping(value = "/admin/product/update", method = RequestMethod.POST)
    public String updateProduct(Model model, @ModelAttribute("product") @Valid Product product,
            BindingResult bindingResult,
            @RequestParam("fileUpload") MultipartFile file) {

        if (bindingResult.hasErrors()) {
            return "/admin/product/update";
        }

        boolean checkChangeImage = false;
        if (!file.isEmpty()) {
            String productAvatar = this.uploadService.handleSaveUploadFile(file, "product");
            product.setImage(productAvatar);
            checkChangeImage = true;
        }

        this.productService.updateProductById(product, checkChangeImage);

        return "redirect:/admin/product";
    }
}
