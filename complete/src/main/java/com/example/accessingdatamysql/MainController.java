package com.example.accessingdatamysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller	// This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
	@Autowired // This means to get the bean called userRepository
			   // Which is auto-generated by Spring, we will use it to handle the data
	private UserRepository userRepository;
	@Autowired
	private ItemRepository itemRepository;

	@PostMapping(path="/add") // Map ONLY POST Requests
	public @ResponseBody String addNewUser (@RequestParam String name
			, @RequestParam String email) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		User n = new User();
		n.setName(name);
		n.setEmail(email);
		userRepository.save(n);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		return userRepository.findAll();
	}

	@GetMapping(path="/items/all")
	public @ResponseBody Iterable<Item> getAllItems(){
		return itemRepository.findAll();
	}

	@PostMapping(path="/items/add") // Map ONLY POST Requests
	public @ResponseBody String addNewItem (@RequestParam Integer type
			, @RequestParam Integer ownerId) {


		Optional<User> userOptional = userRepository.findById(ownerId);
		if(userOptional.isPresent()){

			Item item = new Item();
			item.setType(type);

			User user = userOptional.get();
			user.addItem(item);

			userRepository.save(user);
			//itemRepository.save(item);
		}
		else{
			return "provide correct userId for owner";
		}

		return "Saved Item";
	}
}
