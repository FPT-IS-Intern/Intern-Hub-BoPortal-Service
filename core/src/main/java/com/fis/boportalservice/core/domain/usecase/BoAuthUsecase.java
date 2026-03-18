package com.fis.boportalservice.core.domain.usecase;

import com.fis.boportalservice.core.domain.model.BoAdminProfile;
import com.fis.boportalservice.core.domain.model.BoAuthSession;

public interface BoAuthUsecase {
  BoAuthSession login(String username, String password, String deviceId);

  BoAuthSession refresh(String refreshToken, String deviceId);

  void logout(String refreshToken);

  BoAdminProfile me(String accessToken);
}
