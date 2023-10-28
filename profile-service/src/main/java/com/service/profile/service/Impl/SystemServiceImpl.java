package com.service.profile.service.Impl;

import com.service.profile.exceptions.CardNotFoundException;
import com.service.profile.profiles.Card;
import com.service.profile.repository.CardRepository;
import com.service.profile.service.SystemService;
import com.service.profile.systemDTOs.CardDataTransfer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SystemServiceImpl implements SystemService {
    final CardRepository cardRepository;
    @Autowired
    public SystemServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public String updateCard(CardDataTransfer cardDataTransfer) {
        // get card
        Optional<Card> cardOptional = cardRepository.findById(cardDataTransfer.getId());
        if(cardOptional.isEmpty()){
            throw new CardNotFoundException("Invalid card.");
        }
        Card card = cardOptional.get();
        card.setBalance(cardDataTransfer.getBalance());
        card.setCardLimit(cardDataTransfer.getCardLimit());
        card.setCardStatus(cardDataTransfer.getCardStatus());
        cardRepository.save(card);
        return "Card updated in profile service";
    }
}
