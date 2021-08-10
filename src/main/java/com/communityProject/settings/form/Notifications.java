package com.communityProject.settings.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notifications {
    private boolean postCreatedByEmail;
    private boolean postCreatedByWeb = true;

    private boolean postEnrollmentResultByEmail;
    private boolean postEnrollmentResultByWeb = true;

    private boolean postUpdatedByEmail;
    private boolean postUpdatedByWeb = true;
}
