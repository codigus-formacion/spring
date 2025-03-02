package es.codeurjc.items;

import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

import java.net.URI;
import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @PostConstruct
    public void init(){
        itemRepository.save(new Item("Leche", false));
        itemRepository.save(new Item("Pan", true));
    }

    @GetMapping("/")
    public Collection<Item> getItemRepository() {
        return itemRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItem(@PathVariable long id) {

        Optional<Item> op = itemRepository.findById(id);

        if (op.isPresent()) {
            Item item = op.get();
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<Item> createItem(@RequestBody Item item) {

        itemRepository.save(item);

        URI location = fromCurrentRequest().path("/{id}").buildAndExpand(item.getId()).toUri();

        return ResponseEntity.created(location).body(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> replaceItem(@PathVariable long id, @RequestBody Item updatedItem) {

        if (itemRepository.existsById(id)) {

            updatedItem.setId(id);
            itemRepository.save(updatedItem);

            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Item> deleteItem(@PathVariable long id) {

        Optional<Item> op = itemRepository.findById(id);

        if (op.isPresent()) {
			Item item = op.get();
            itemRepository.deleteById(id);
            return ResponseEntity.ok(item);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
