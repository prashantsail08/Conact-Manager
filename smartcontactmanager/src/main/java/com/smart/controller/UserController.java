package com.smart.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.sql.Blob;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Positive;

import org.hibernate.annotations.Check;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smart.config.helper.Message;
import com.smart.dao.ContactRepository;
import com.smart.dao.UserRepository;
import com.smart.entities.Contact;
import com.smart.entities.User;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	// method for add common data to respomse
	@ModelAttribute
	public void addCommondata(Model model, Principal principal) {
		
		System.out.println("addCommondata : ");

		String userName = principal.getName();
		System.out.println("USERNAME :" + userName);

		// get the user using userName(email)
		User user = this.userRepository.getUsersByUserName(userName);

		System.out.println("USER :" + user);
		// send data to dashboard
		model.addAttribute("user", user);

	}

	// user dashboard home
	@RequestMapping("/index")
	public String dashboard(Model model, Principal principal) // with the help of principal we can find username unique
																// identifier
	{
		System.out.println("dashboard : ");
		
		model.addAttribute("title", "User Dashboard");
		return "normal/dash_board";
	}

	// open add form handler
	@RequestMapping("/add_contact")
	public String openAddContactForm(Model model) {
		
		System.out.println("openAddContactForm : ");
		
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

	// processing add contact from
	@RequestMapping(value = "/process-contact", method = RequestMethod.POST)
	public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Principal principal, HttpSession session) {

		try {
			
			System.out.println("processContact : ");
			
			String name = principal.getName();

			User user = this.userRepository.getUsersByUserName(name);

			// processing and uploading file...
			if (file.isEmpty()) {
				// if the file is empty then try our message
				System.out.println("Image is empty");
				contact.setImage("contact.png"); // for default img
			} else {
				// if the file is empty then try our message
				contact.setImage(file.getOriginalFilename());

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				System.out.println("Image is Uploaded");

			}

			contact.setUser(user);

			user.getContacts().add(contact);

			this.userRepository.save(user);
			System.out.println("Added to Data base");

			System.out.println("DATA :" + contact);

			// message success........
			session.setAttribute("message", new Message("Your Contact is Added !! add more...", "success"));

		} catch (Exception e) {
			System.out.println("processContact : ");
			
			System.out.println("ERROR " + e.getMessage());
			e.printStackTrace();

			// error message
			session.setAttribute("message", new Message("Something went Wrong !! Try Again...", "danger"));
		}
		return "normal/add_contact_form";
	}

	// Show Contacts handler

	// per page -5[n]
	// Current page = 0 [page]
	@GetMapping("/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page, Model model, Principal principal) {
		
		System.out.println("showContacts : ");
		
		model.addAttribute("title", "Show User Contacts");

		// send contacts list we can use this method by using principal
//		String userName = principal.getName();
//		
//		User user = this.userRepository.getUsersByUserName(userName);
//		List<Contact> contacts = user.getContacts();

		// send contacts list
		String userName = principal.getName();
		User user = this.userRepository.getUsersByUserName(userName);

		// CurrentPage -
		// Contact per page-5
		Pageable pageable = PageRequest.of(page, 5); // we change number for list to show

		// our userName was email so we use user.getId so it will give us id of user
		Page<Contact> contacts = this.contactRepository.findContactsByUser(user.getId(), pageable);

		model.addAttribute("contacts", contacts);
		// pagination...
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());

		return "normal/show_contact";
	}

	// showing particular contact Details
	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId") Integer cId, Model model, Principal principal) {
		
		System.out.println("showContactDetail : ");
		
		System.out.println("showContactDetail: cId :" + cId);

		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();

		//
		String userName = principal.getName();
		User user = this.userRepository.getUsersByUserName(userName);

		if (user.getId() == contact.getUser().getId()) {

			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}

		return "normal/contact_detail";
	}

	// delete contact handler
	@GetMapping("/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Principal principal, HttpSession session) {
		
		System.out.println("deleteContact : ");
		
		System.out.println("CID " + cId);

		Contact contact = this.contactRepository.findById(cId).get();

		// check..Assign
		System.out.println("Contact " + contact.getcId());

		// contact.setUser(null);

//		this.contactRepository.delete(contact);

		User user = this.userRepository.getUsersByUserName(principal.getName());
		user.getContacts().remove(contact);

		this.userRepository.save(user);

		System.out.println("DELETED");

		session.setAttribute("message", new Message("Contact deleted succesfully.....", "success"));

		return "redirect:/user/show-contacts/0";
	}

	// open update from handler
	@PostMapping("/update-contact/{cid}")
	public String updateForm(@PathVariable("cid") Integer cid, Model model) {
		
		System.out.println("updateForm : ");
		
		model.addAttribute("title", "Update Contact");

		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);

		return "normal/update_form";
	}

	// update contact handler
	@RequestMapping(value = "/process-update", method = RequestMethod.POST)
	public String updateHandler(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
			Model model, HttpSession session, Principal principal) {

		try {
			
			System.out.println("updateHandler : ");

			// old contact detail
			Contact oldContatDetail = this.contactRepository.findById(contact.getcId()).get();

			// image...
			if (!file.isEmpty()) {
				// file work rewrite. old file delete new file add

//				delete old photo
				File deleteFile = new ClassPathResource("static/img").getFile();
				File file1 = new File(deleteFile, oldContatDetail.getImage());
				file1.delete();

//				update new photo

				File saveFile = new ClassPathResource("static/img").getFile();

				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				contact.setImage(file.getOriginalFilename());
				System.out.println("Image is Uploaded");

			} else {
				contact.setImage(oldContatDetail.getImage());
			}

			User user = this.userRepository.getUsersByUserName(principal.getName());

			contact.setUser(user);

			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your Contact is Updated !!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("CONTACT NAME " + contact.getName());
		System.out.println("CONTACT ID " + contact.getcId());

		return "redirect:/user/" + contact.getcId() + "/contact";
	}
	//your Profile handler
	@GetMapping("/profile")
	public String yourProfile(Model model) {
		
		System.out.println("yourProfile : ");
		
		model.addAttribute("title", "Profile Page");
		return "normal/profile";
	}
	
	//open setting handler
	@GetMapping("/settings")
	public String openSettings() {
		return "normal/setting";
	}
	
	//change password handleer
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, Principal principal
			,HttpSession session){
		System.out.println("OLD PASSWORD : "+ oldPassword);
		System.out.println("NEW PASSWORD : " + newPassword);
		
		String userName = principal.getName();
		User currentUser = this.userRepository.getUsersByUserName(userName);
		
		if(this.bCryptPasswordEncoder.matches(oldPassword, currentUser.getPassword()))
		{
			//change the password
			
			currentUser.setPassword(this.bCryptPasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			session.setAttribute("message", new Message("Your Password is Successfuly Changed !!!", "success"));
			
			
			
		}else {
			//error
			session.setAttribute("message", new Message("Please enter correct Old Password !!!", "danger"));
			return "redirect:/user/settings";
		}
		
		
		return "redirect:/user/index";
	}

}
