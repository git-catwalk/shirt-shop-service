package com.bluntsoftware.shirtshop.integrations.types;

import com.bluntsoftware.shirtshop.integrations.types.google.config.GoogleConfig;
import com.bluntsoftware.shirtshop.integrations.types.google.oauth.GDriveOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.quick_books.config.QuickbooksOauthConfig;
import com.bluntsoftware.shirtshop.integrations.types.quick_books.oauth.QuickBooksOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.square.config.SquareConfig;
import com.bluntsoftware.shirtshop.integrations.types.square.oauth.SquareOAuthIntegration;
import com.bluntsoftware.shirtshop.integrations.types.wave.config.WaveConfig;
import com.bluntsoftware.shirtshop.integrations.types.wave.oauth.WaveOAuthIntegration;
import org.springframework.stereotype.Component;

@Component
public class OAuthIntegrationFactory {

    private final GoogleConfig googleConfig;
    private final QuickbooksOauthConfig qbConfig;
    private final WaveConfig waveConfig;
    private final SquareConfig squareConfig;
    public OAuthIntegrationFactory(GoogleConfig googleConfig, QuickbooksOauthConfig qbConfig, WaveConfig waveConfig, SquareConfig squareConfig) {
        this.googleConfig = googleConfig;
        this.qbConfig = qbConfig;
        this.waveConfig = waveConfig;
        this.squareConfig = squareConfig;
    }

    public OAuthIntegration createIntegration(String type) {
        if(GDriveOAuthIntegration.GOOGLE_DRIVE_ID.equalsIgnoreCase(type)) {
            return new GDriveOAuthIntegration(this.googleConfig);
        }

        if(QuickBooksOAuthIntegration.QUICK_BOOKS_ID.equalsIgnoreCase(type)) {
            return new QuickBooksOAuthIntegration(this.qbConfig);
        }

        if(WaveOAuthIntegration.WAVE_ID.equalsIgnoreCase(type)) {
            return new WaveOAuthIntegration(this.waveConfig);
        }

        if(SquareOAuthIntegration.SQUARE_ID.equalsIgnoreCase(type)) {
            return new SquareOAuthIntegration(this.squareConfig);
        }

        return null;
    }

}
